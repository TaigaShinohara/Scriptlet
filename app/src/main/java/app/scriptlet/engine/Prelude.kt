package app.scriptlet.engine

/**
 * JS-side API (Scriptable ListWidget subset), implemented in pure JS so the same
 * prelude serves every engine binding. It builds the widget tree as plain objects;
 * the host serializes the finished tree to JSON.
 *
 * Conventions for user scripts:
 *  - build a `new ListWidget()`, then call `Script.setWidget(widget)`
 *    (or just declare `const widget = new ListWidget()` and the host picks it up)
 *  - `await fetch(url)` returns { ok, status, text(), json() }
 *  - `console.log(...)` goes to the in-app log view
 *  - `Storage.get(key)` / `Storage.set(key, value)` is a small per-script store
 */
object Prelude {
    const val SOURCE = """
globalThis.__widgetTree = null;
globalThis.__logs = [];

class Color {
  constructor(hex) { this.hex = hex; }
}
function __colorHex(c) { return c == null ? null : (c.hex !== undefined ? c.hex : String(c)); }

class Font {
  static systemFont(size) { return { size: size, bold: false }; }
  static boldSystemFont(size) { return { size: size, bold: true }; }
}

class TextHandle {
  constructor(node) {
    this.node = node;
    const self = this;
    Object.defineProperty(this, 'align', {
      set(v) { self.node.align = String(v); },
      get() { return self.node.align; }
    });
  }
  set font(f) { if (f) { this.node.fontSize = f.size; this.node.bold = !!f.bold; } }
  get font() { return { size: this.node.fontSize, bold: this.node.bold }; }
  set textColor(c) { this.node.color = __colorHex(c); }
  get textColor() { return this.node.color; }
}

class ImageHandle {
  constructor(node) {
    this.node = node;
    const self = this;
    Object.defineProperty(this, 'width', {
      set(v) { self.node.width = v | 0; },
      get() { return self.node.width; }
    });
    Object.defineProperty(this, 'height', {
      set(v) { self.node.height = v | 0; },
      get() { return self.node.height; }
    });
  }
  setSize(w, h) { this.node.width = w | 0; this.node.height = h | 0; }
}

function __newContainer(type) {
  return { type: type, direction: 'vertical', children: [], backgroundColor: null, padding: [0,0,0,0], spacing: 0, cornerRadius: 0, width: 0, height: 0, alignItems: 'start' };
}

class CanvasHandle {
  constructor(node) { this.node = node; }
  // Cap recorded ops so a runaway loop cannot produce an unbounded tree.
  __op(o) { if (this.node.ops.length < 2000) this.node.ops.push(o); return this; }
  background(color) { return this.__op({ op: 'bg', color: __colorHex(color) }); }
  rect(x, y, w, h, color) { return this.__op({ op: 'rect', x: x, y: y, w: w, h: h, color: __colorHex(color) }); }
  roundRect(x, y, w, h, r, color) { return this.__op({ op: 'roundRect', x: x, y: y, w: w, h: h, r: r, color: __colorHex(color) }); }
  circle(cx, cy, r, color) { return this.__op({ op: 'circle', cx: cx, cy: cy, r: r, color: __colorHex(color) }); }
  // Pie slice: angles in degrees, 0 = right (3 o'clock), positive = clockwise.
  arc(cx, cy, r, startDeg, sweepDeg, color) { return this.__op({ op: 'arc', cx: cx, cy: cy, r: r, start: startDeg, sweep: sweepDeg, color: __colorHex(color) }); }
  // Stroked arc (gauge): same angles as arc, drawn as a thick line.
  ring(cx, cy, r, width, startDeg, sweepDeg, color) { return this.__op({ op: 'ring', cx: cx, cy: cy, r: r, width: width, start: startDeg, sweep: sweepDeg, color: __colorHex(color) }); }
  line(x1, y1, x2, y2, color, width) { return this.__op({ op: 'line', x1: x1, y1: y1, x2: x2, y2: y2, color: __colorHex(color), width: width || 2 }); }
  // y is the TOP of the text.
  text(x, y, str, size, color, bold) { return this.__op({ op: 'text', x: x, y: y, value: String(str), size: size || 12, color: __colorHex(color), bold: !!bold }); }
}

class StackHandle {
  constructor(node) {
    this.node = node;
    const self = this;
    Object.defineProperty(this, 'backgroundColor', {
      set(c) { self.node.backgroundColor = __colorHex(c); },
      get() { return self.node.backgroundColor; }
    });
    Object.defineProperty(this, 'cornerRadius', {
      set(v) { self.node.cornerRadius = v | 0; },
      get() { return self.node.cornerRadius; }
    });
    Object.defineProperty(this, 'spacing', {
      set(v) { self.node.spacing = v | 0; },
      get() { return self.node.spacing; }
    });
    Object.defineProperty(this, 'alignItems', {
      set(v) { self.node.alignItems = String(v); },
      get() { return self.node.alignItems; }
    });
    Object.defineProperty(this, 'width', {
      set(v) { self.node.width = v | 0; },
      get() { return self.node.width; }
    });
    Object.defineProperty(this, 'height', {
      set(v) { self.node.height = v | 0; },
      get() { return self.node.height; }
    });
  }
  layoutHorizontally() { this.node.direction = 'horizontal'; }
  layoutVertically() { this.node.direction = 'vertical'; }
  setPadding(top, left, bottom, right) { this.node.padding = [top|0, left|0, bottom|0, right|0]; }
  setSize(w, h) { this.node.width = w | 0; this.node.height = h | 0; }
  addText(t) {
    const n = { type: 'text', value: String(t), fontSize: 14, bold: false, color: null, align: 'left' };
    this.node.children.push(n);
    return new TextHandle(n);
  }
  addStack() {
    const n = __newContainer('stack');
    this.node.children.push(n);
    return new StackHandle(n);
  }
  addSpacer(len) { this.node.children.push({ type: 'spacer', length: (len === undefined || len === null) ? -1 : (len | 0) }); }
  addDivider(color) {
    const n = { type: 'divider', color: color == null ? null : __colorHex(color) };
    this.node.children.push(n);
  }
  addImage(source, w, h) {
    let kind = 'url';
    let src = source;
    if (source && typeof source === 'object') {
      if (source.data !== undefined) { kind = 'base64'; src = String(source.data); }
      else { src = String(source.url); }
    } else {
      src = String(source);
      if (src.indexOf('data:') === 0) { kind = 'base64'; src = src.substring(src.indexOf(',') + 1); }
    }
    const n = { type: 'image', imageSource: src, imageKind: kind, width: (w | 0) || 24, height: (h | 0) || 24 };
    this.node.children.push(n);
    return new ImageHandle(n);
  }
  addProgress(value, color, bgColor) {
    const v = Math.max(0, Math.min(1, Number(value) || 0));
    const n = { type: 'progress', progress: v, color: color == null ? null : __colorHex(color), backgroundColor: bgColor == null ? null : __colorHex(bgColor) };
    this.node.children.push(n);
  }
  addCanvas(w, h) {
    const n = { type: 'canvas', width: (w | 0) || 100, height: (h | 0) || 100, ops: [] };
    this.node.children.push(n);
    return new CanvasHandle(n);
  }
  addLink(t, url) {
    const n = { type: 'text', value: String(t), fontSize: 14, bold: false, color: null, align: 'left', url: String(url) };
    this.node.children.push(n);
    return new TextHandle(n);
  }
  addLayer() {
    const n = __newContainer('layer');
    this.node.children.push(n);
    return new StackHandle(n);
  }
}

class ListWidget {
  constructor() {
    this.node = __newContainer('widget');
    this.__stack = new StackHandle(this.node);
    const self = this;
    Object.defineProperty(this, 'backgroundColor', {
      set(c) { self.node.backgroundColor = __colorHex(c); },
      get() { return self.node.backgroundColor; }
    });
  }
  setPadding(top, left, bottom, right) { this.__stack.setPadding(top, left, bottom, right); }
  addText(t) { return this.__stack.addText(t); }
  addStack() { return this.__stack.addStack(); }
  addSpacer(len) { this.__stack.addSpacer(len); }
  addDivider(color) { this.__stack.addDivider(color); }
  addImage(source, w, h) { return this.__stack.addImage(source, w, h); }
  addProgress(value, color, bgColor) { this.__stack.addProgress(value, color, bgColor); }
  addCanvas(w, h) { return this.__stack.addCanvas(w, h); }
  addLink(t, url) { return this.__stack.addLink(t, url); }
  addLayer() { return this.__stack.addLayer(); }
}

// Chart helpers: PieChart(cv, cx, cy, r, [[value, color], ...]) draws a pie starting at the top.
// BarChart(cv, x, y, w, h, [v...], max, color, bgColor) draws vertical bars on a baseline.
globalThis.PieChart = function(cv, cx, cy, r, parts) {
  let total = 0;
  for (const p of parts) total += p[0];
  if (total <= 0) return;
  let start = -90;
  for (const p of parts) {
    const sweep = (p[0] / total) * 360;
    cv.arc(cx, cy, r, start, sweep, p[1]);
    start += sweep;
  }
};
globalThis.BarChart = function(cv, x, y, w, h, values, max, color, bgColor) {
  if (bgColor) cv.rect(x, y, w, h, bgColor);
  const n = values.length;
  if (n === 0) return;
  const slot = w / n;
  const bw = Math.max(2, slot * 0.6);
  const m = max || Math.max.apply(null, values.concat([1]));
  for (let i = 0; i < n; i++) {
    const bh = Math.max(1, (values[i] / m) * (h - 4));
    cv.roundRect(x + i * slot + (slot - bw) / 2, y + h - bh, bw, bh, 2, color);
  }
};

globalThis.Icons = {
  check: '✓', cross: '✗', warn: '⚠', fire: '🔥', star: '⭐', pin: '📌',
  bell: '🔔', clock: '🕐', calendar: '📅', chart: '📊', chartUp: '📈',
  mail: '📮', refresh: '🔄', rocket: '🚀', target: '🎯', bulb: '💡',
  gear: '⚙️', link: '🔗', lock: '🔒', heart: '❤️', memo: '📝',
  sun: '☀️', cloud: '☁️', rain: '🌧️', snow: '❄️', moon: '🌙',
  music: '🎵', game: '🎮', book: '📚', coffee: '☕', food: '🍽️',
  train: '🚃', car: '🚗', plane: '✈️', home: '🏠', work: '💼',
  money: '💰', run: '🏃', muscle: '💪', sleep: '😴', camera: '📷',
  video: '🎬', mic: '🎙️',
  x: '✖', threads: '🧵', bluesky: '🦋', mastodon: '🐘', line: '💬',
  reddit: '👽', news: '📰', youtube: '▶', instagram: '📷', note: '📝',
  red: '🔴', orange: '🟠', yellow: '🟡', green: '🟢', blue: '🔵',
  purple: '🟣', white: '⚪', black: '⚫'
};

globalThis.Color = Color;
globalThis.Font = Font;
globalThis.ListWidget = ListWidget;
globalThis.Script = {
  setWidget(w) { globalThis.__widgetTree = w && w.node ? w.node : w; },
  // Phase A (prototype): declare this script as animated. Only meaningful on
  // the dedicated animation widget lane; normal widgets render frame 0 as a
  // still image. While animating, the host re-runs the script per frame with
  // the globals `frame` (0,1,2,...) and `elapsedMs` refreshed each time.
  animate(opts) {
    const f = opts && opts.fps ? (opts.fps | 0) : 5;
    globalThis.__animation = { fps: Math.max(1, Math.min(10, f)) };
  }
};
globalThis.console = {
  log: function() {
    const parts = [];
    for (let i = 0; i < arguments.length; i++) {
      const a = arguments[i];
      parts.push(typeof a === 'object' ? JSON.stringify(a) : String(a));
    }
    globalThis.__logs.push(parts.join(' '));
    __log(parts.join(' '));
  }
};
globalThis.fetch = async function(url) {
  const body = await __fetchText(String(url));
  return {
    ok: true,
    status: 200,
    text: async function() { return body; },
    json: async function() { return JSON.parse(body); }
  };
};
globalThis.Storage = {
  get: function(key) { return __storageGet(String(key)); },
  set: function(key, value) { __storageSet(String(key), String(value)); }
};
"""
}
