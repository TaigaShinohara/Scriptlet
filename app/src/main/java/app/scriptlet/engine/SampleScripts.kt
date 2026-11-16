package app.scriptlet.engine

object SampleScripts {
    /** Stable keys, index-aligned with [all]; used to seed new samples on upgrade without resurrecting deleted ones. */
    val keys: List<String> = listOf(
        "showcase-weather",
        "quake",
        "fx-chart",
        "analog-clock",
        "iss",
        "moon",
        "days-since",
        "daily-art",
        "world-clock",
        "weekly-calendar",
        "progress-demo",
        "stylish-clock",
        "showcase-countdown",
        "hello"
    )

    /** One-line descriptions, index-aligned with [keys]/[all]; shown wherever a script is picked. */
    val descs: List<String> = listOf(
        "東京の天気・気温グラフ・日の出入り（本物のデータ）",
        "日本の最新地震を震度つきで表示（本物のデータ）",
        "過去30営業日のドル円を折れ線グラフで（本物のデータ）",
        "Canvasで目盛りと針まで描くアナログ時計",
        "国際宇宙ステーションの現在位置と大阪からの距離（本物のデータ）",
        "月齢と満ち欠けを計算して描く（ネット不要）",
        "開始日を覚えて経過日数を数え続ける（Storage）",
        "日付で絵が変わるジェネラティブアート（Canvas）",
        "5都市の現在時刻。夏時間も自動で判定",
        "今月のカレンダーをCanvasで描く（ネット不要）",
        "今日・今週・今月・今年の経過率をバーで表示",
        "大きな時計と今日の経過リング",
        "今年の残り日数を円ゲージで表示",
        "文字と現在時刻を出す、いちばん小さな例"
    )

    /** The showcase presets: full real-world widgets, offered first when creating. */
    val featuredKeys: Set<String> = setOf("showcase-weather", "quake", "fx-chart", "analog-clock")

    /**
     * The bundled templates seeded into the script list on first launch
     * (user direction, 2026-11-16: keep the out-of-box list to just these
     * two). Everything else in [all] stays available from the
     * "サンプルから作る" picker without being pre-seeded.
     */
    val templateKeys: Set<String> = setOf("showcase-weather", "showcase-countdown")

    fun descForName(name: String): String? {
        val idx = all.indexOfFirst { it.first == name }
        return if (idx >= 0) descs.getOrNull(idx) else null
    }

    val all: List<Pair<String, String>> = listOf(
        "🌤 東京の天気（実データ）" to """
// プリセット: 東京の天気 2.0（Open-Meteo、APIキー不要の実データ）
// fetch・Canvas棒グラフ・Storageキャッシュ（圏外でも前回データを出す）の組み合わせ
function codeInfo(c) {
  if (c === 0) return ["☀️", "快晴"];
  if (c <= 2) return ["🌤️", "晴れ"];
  if (c === 3) return ["☁️", "曇り"];
  if (c === 45 || c === 48) return ["🌫️", "霧"];
  if (c >= 51 && c <= 57) return ["🌦️", "霧雨"];
  if (c >= 61 && c <= 67) return ["🌧️", "雨"];
  if (c >= 71 && c <= 77) return ["🌨️", "雪"];
  if (c >= 80 && c <= 82) return ["🌦️", "にわか雨"];
  if (c >= 85 && c <= 86) return ["🌨️", "雪"];
  if (c >= 95) return ["⛈️", "雷雨"];
  return ["🌡️", "—"];
}
const pad2 = function (n) { return String(n).padStart(2, "0"); };

async function loadWeather() {
  const url = "https://api.open-meteo.com/v1/forecast?latitude=35.6762&longitude=139.6503&current=temperature_2m,apparent_temperature,weather_code,relative_humidity_2m&hourly=temperature_2m&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_probability_max&forecast_days=2&timezone=Asia%2FTokyo";
  try {
    const res = await fetch(url);
    const d = await res.json();
    Storage.set("wx_cache", JSON.stringify(d));
    Storage.set("wx_cache_at", String(Date.now()));
    return { d: d, cachedAt: 0 };
  } catch (e) {
    const c = Storage.get("wx_cache");
    if (c) return { d: JSON.parse(c), cachedAt: Number(Storage.get("wx_cache_at") || 0) };
    throw e;
  }
}

const widget = new ListWidget();
widget.backgroundColor = new Color("#1d3242");
widget.setPadding(14, 14, 14, 14);

const now = new Date();
const head = widget.addStack();
head.layoutHorizontally();
head.alignItems = "center";
const title = head.addText("🌤 東京の天気");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#7cc7f0");
head.addSpacer();

try {
  const loaded = await loadWeather();
  const d = loaded.d;
  const upd = head.addText(
    loaded.cachedAt > 0
      ? "前回 " + pad2(new Date(loaded.cachedAt).getHours()) + ":" + pad2(new Date(loaded.cachedAt).getMinutes())
      : "更新 " + pad2(now.getHours()) + ":" + pad2(now.getMinutes())
  );
  upd.font = Font.systemFont(10);
  upd.textColor = new Color(loaded.cachedAt > 0 ? "#ffab7a" : "#9ca3af");

  widget.addSpacer(8);
  const info = codeInfo(d.current.weather_code);
  const big = widget.addStack();
  big.layoutHorizontally();
  big.alignItems = "center";
  const em = big.addText(info[0]);
  em.font = Font.systemFont(30);
  const temp = big.addText(" " + Math.round(d.current.temperature_2m) + "°");
  temp.font = Font.boldSystemFont(34);
  temp.textColor = new Color("#ffffff");
  const lab = big.addText(" " + info[1]);
  lab.font = Font.systemFont(14);
  lab.textColor = new Color("#e0f2fb");
  const feel = big.addText("  体感 " + Math.round(d.current.apparent_temperature) + "°");
  feel.font = Font.systemFont(11);
  feel.textColor = new Color("#9ca3af");

  const rainMax = d.daily.precipitation_probability_max[0] || 0;
  const sub = widget.addText(
    "最高 " + Math.round(d.daily.temperature_2m_max[0]) + "° ／ 最低 " + Math.round(d.daily.temperature_2m_min[0]) + "°" +
    " ・ 湿度 " + d.current.relative_humidity_2m + "% ・ 降水確率 " + rainMax + "%"
  );
  sub.font = Font.systemFont(12);
  sub.textColor = new Color("#cbd5e1");

  widget.addSpacer(8);
  const temps = [];
  for (let h = 0; h < 24; h += 2) temps.push(d.hourly.temperature_2m[h]);
  const cv = widget.addCanvas(232, 46);
  BarChart(cv, 0, 0, 232, 46, temps, Math.max.apply(null, temps) + 2, "#7cc7f0", null);

  const tmr = codeInfo(d.daily.weather_code[1]);
  const rise = String(d.daily.sunrise[0]).slice(11, 16);
  const set = String(d.daily.sunset[0]).slice(11, 16);
  const foot = widget.addText(
    "🌅 " + rise + " 🌇 " + set + " ｜ 明日 " + tmr[0] + tmr[1] +
    " " + Math.round(d.daily.temperature_2m_max[1]) + "°/" + Math.round(d.daily.temperature_2m_min[1]) + "°"
  );
  foot.font = Font.systemFont(11);
  foot.textColor = new Color("#9ca3af");
} catch (e) {
  const err = widget.addText("取得に失敗: " + e.message);
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}

Script.setWidget(widget);
""".trimIndent(),
        "🚨 地震情報" to """
// プリセット: 地震情報（P2P地震情報API、キー不要の実データ）
// 最新の地震を最大震度つきで表示。取得に失敗したら前回データを出す
function scaleInfo(v) {
  switch (v) {
    case 10: return ["1", "#9ca3af"];
    case 20: return ["2", "#7cc7f0"];
    case 30: return ["3", "#7cc7f0"];
    case 40: return ["4", "#facc15"];
    case 45: return ["5弱", "#e65100"];
    case 50: return ["5強", "#e65100"];
    case 55: return ["6弱", "#c2185b"];
    case 60: return ["6強", "#c2185b"];
    case 70: return ["7", "#6a1b9a"];
    default: return ["?", "#9ca3af"];
  }
}

async function loadQuakes() {
  try {
    const res = await fetch("https://api.p2pquake.net/v2/history?codes=551&limit=5");
    const d = await res.json();
    Storage.set("quake_cache", JSON.stringify(d));
    return { list: d, cached: false };
  } catch (e) {
    const c = Storage.get("quake_cache");
    if (c) return { list: JSON.parse(c), cached: true };
    throw e;
  }
}

const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const head = widget.addStack();
head.layoutHorizontally();
head.alignItems = "center";
const title = head.addText("🚨 地震情報");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
head.addSpacer();

try {
  const loaded = await loadQuakes();
  const list = loaded.list;
  if (loaded.cached) {
    const c = head.addText("前回データ");
    c.font = Font.systemFont(10);
    c.textColor = new Color("#ffab7a");
  }
  widget.addSpacer(8);

  const items = [];
  for (const q of list) {
    if (q && q.earthquake && q.earthquake.hypocenter) items.push(q);
  }
  if (items.length === 0) throw new Error("データが空です");

  const q0 = items[0].earthquake;
  const s0 = scaleInfo(q0.maxScale);
  const row = widget.addStack();
  row.layoutHorizontally();
  row.alignItems = "center";
  const badge = row.addStack();
  badge.setPadding(6, 10, 6, 10);
  badge.cornerRadius = 10;
  badge.backgroundColor = new Color(s0[1]);
  const bt = badge.addText("震度" + s0[0]);
  bt.font = Font.boldSystemFont(17);
  bt.textColor = new Color("#ffffff");
  const side = row.addStack();
  side.setPadding(0, 10, 0, 0);
  const place = side.addText(q0.hypocenter.name || "震源不明");
  place.font = Font.boldSystemFont(16);
  place.textColor = new Color("#ffffff");
  const mag = q0.hypocenter.magnitude >= 0 ? "M" + q0.hypocenter.magnitude.toFixed(1) : "M—";
  const depth = q0.hypocenter.depth >= 0 ? "深さ" + q0.hypocenter.depth + "km" : "深さ不明";
  const when = String(q0.time).slice(5, 16);
  const meta = side.addText(when + " ・ " + mag + " ・ " + depth);
  meta.font = Font.systemFont(11);
  meta.textColor = new Color("#9ca3af");
  if (q0.domesticTsunami && q0.domesticTsunami !== "None") {
    const ts = side.addText("⚠ 津波情報あり");
    ts.font = Font.boldSystemFont(11);
    ts.textColor = new Color("#ffab7a");
  }

  for (let i = 1; i < Math.min(3, items.length); i++) {
    const q = items[i].earthquake;
    const s = scaleInfo(q.maxScale);
    widget.addSpacer(6);
    const line = widget.addText(
      "震度" + s[0] + "  " + (q.hypocenter.name || "") + "  " +
      (q.hypocenter.magnitude >= 0 ? "M" + q.hypocenter.magnitude.toFixed(1) : "") +
      "  " + String(q.time).slice(5, 16)
    );
    line.font = Font.systemFont(11);
    line.textColor = new Color("#cbd5e1");
  }
} catch (e) {
  const err = widget.addText("取得に失敗: " + e.message);
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}

Script.setWidget(widget);
""".trimIndent(),
        "💱 ドル円チャート" to """
// プリセット: ドル円チャート（Frankfurter、キー不要の実データ）
// 過去45日分の時系列を取り、Canvasに折れ線グラフを描く
function ymd(d) {
  return d.getFullYear() + "-" + String(d.getMonth() + 1).padStart(2, "0") + "-" + String(d.getDate()).padStart(2, "0");
}

async function loadRates() {
  const now = new Date();
  const start = new Date(now.getTime() - 45 * 86400000);
  const url = "https://api.frankfurter.dev/v1/" + ymd(start) + ".." + ymd(now) + "?from=USD&to=JPY";
  try {
    const res = await fetch(url);
    const d = await res.json();
    Storage.set("fx_cache", JSON.stringify(d));
    return { d: d, cached: false };
  } catch (e) {
    const c = Storage.get("fx_cache");
    if (c) return { d: JSON.parse(c), cached: true };
    throw e;
  }
}

const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const head = widget.addStack();
head.layoutHorizontally();
head.alignItems = "center";
const title = head.addText("💱 ドル円");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
head.addSpacer();

try {
  const loaded = await loadRates();
  const rates = loaded.d.rates;
  const dates = Object.keys(rates).sort();
  if (dates.length < 2) throw new Error("データ不足");
  const values = dates.map(function (k) { return rates[k].JPY; });
  const last = values[values.length - 1];
  const first = values[0];
  const diff = last - first;
  const when = head.addText(dates[dates.length - 1].slice(5) + (loaded.cached ? "（前回）" : ""));
  when.font = Font.systemFont(10);
  when.textColor = new Color("#9ca3af");

  widget.addSpacer(6);
  const bigRow = widget.addStack();
  bigRow.layoutHorizontally();
  bigRow.alignItems = "center";
  const rate = bigRow.addText(last.toFixed(2) + " 円");
  rate.font = Font.boldSystemFont(30);
  rate.textColor = new Color("#ffffff");
  const chg = bigRow.addText("  " + (diff >= 0 ? "▲ +" : "▼ ") + diff.toFixed(2) + "（" + values.length + "営業日）");
  chg.font = Font.boldSystemFont(13);
  chg.textColor = new Color(diff >= 0 ? "#f78fb8" : "#7cc7f0");

  widget.addSpacer(8);
  const W = 232, H = 62;
  let min = Math.min.apply(null, values);
  let max = Math.max.apply(null, values);
  const padV = (max - min) * 0.15 || 1;
  min -= padV; max += padV;
  const cv = widget.addCanvas(W, H);
  for (let g = 0; g < 3; g++) {
    const gy = Math.round((H / 2) * g) + 0.5;
    cv.line(0, gy, W, gy, "#1f2937", 1);
  }
  for (let i = 1; i < values.length; i++) {
    const x1 = (i - 1) / (values.length - 1) * (W - 4) + 2;
    const x2 = i / (values.length - 1) * (W - 4) + 2;
    const y1 = H - 6 - (values[i - 1] - min) / (max - min) * (H - 14);
    const y2 = H - 6 - (values[i] - min) / (max - min) * (H - 14);
    cv.line(x1, y1, x2, y2, "#7cc7f0", 2);
  }
  const lx = W - 2;
  const ly = H - 6 - (last - min) / (max - min) * (H - 14);
  cv.circle(lx - 2, ly, 3, "#f78fb8");
  cv.text(2, 0, max.toFixed(0), 9, "#9ca3af", false);
  cv.text(2, H - 11, min.toFixed(0), 9, "#9ca3af", false);

  const foot = widget.addText("欧州中央銀行の参照レート（平日のみ更新）");
  foot.font = Font.systemFont(10);
  foot.textColor = new Color("#6b7280");
} catch (e) {
  const err = widget.addText("取得に失敗: " + e.message);
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}

Script.setWidget(widget);
""".trimIndent(),
        "🕐 アナログ時計" to """
// プリセット: アナログ時計（Canvasで文字盤・目盛り・針まで全部描く）
// ウィジェットは更新時点の時刻を表示します（タップで再描画）
const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(12, 12, 12, 12);

const now = new Date();
const dow = "日月火水木金土"[now.getDay()];
const dateText = widget.addText((now.getMonth() + 1) + "月" + now.getDate() + "日（" + dow + "）");
dateText.font = Font.boldSystemFont(13);
dateText.textColor = new Color("#f78fb8");
dateText.align = "center";

const row = widget.addStack();
row.layoutHorizontally();
row.addSpacer();
const S = 148, C = S / 2, R = 70;
const cv = row.addCanvas(S, S);
cv.circle(C, C, R + 4, "#1f2937");

function hand(angleDeg, len) {
  const rad = angleDeg * Math.PI / 180;
  return [C + len * Math.sin(rad), C - len * Math.cos(rad)];
}
// 目盛り（60本、5分刻みは太く）
for (let m = 0; m < 60; m++) {
  const big = m % 5 === 0;
  const a = m * 6;
  const p1 = hand(a, big ? 56 : 61);
  const p2 = hand(a, 66);
  cv.line(p1[0], p1[1], p2[0], p2[1], big ? "#e5e7eb" : "#4b5563", big ? 2 : 1);
}
// 数字（12・3・6・9）
cv.text(C - 6, 10, "12", 12, "#9ca3af", true);
cv.text(S - 18, C - 6, "3", 12, "#9ca3af", true);
cv.text(C - 3, S - 22, "6", 12, "#9ca3af", true);
cv.text(10, C - 6, "9", 12, "#9ca3af", true);

const h = now.getHours(), mi = now.getMinutes(), se = now.getSeconds();
const hp = hand((h % 12) * 30 + mi * 0.5, 38);
cv.line(C, C, hp[0], hp[1], "#ffffff", 6);
const mp = hand(mi * 6 + se * 0.1, 54);
cv.line(C, C, mp[0], mp[1], "#ffffff", 4);
const sp = hand(se * 6, 60);
cv.line(C, C, sp[0], sp[1], "#f78fb8", 2);
cv.circle(C, C, 4, "#f78fb8");
row.addSpacer();

const cap = widget.addText("タップで更新");
cap.font = Font.systemFont(10);
cap.textColor = new Color("#6b7280");
cap.align = "center";

Script.setWidget(widget);
""".trimIndent(),
        "🛰 ISSはいまここ" to """
// プリセット: ISSはいまここ（wheretheiss.at、キー不要の実データ）
// 国際宇宙ステーションの現在位置と、大阪からの距離を表示
const OSAKA = { lat: 34.6937, lon: 135.5023 };
function distKm(lat1, lon1, lat2, lon2) {
  const R = 6371;
  const dLat = (lat2 - lat1) * Math.PI / 180;
  const dLon = (lon2 - lon1) * Math.PI / 180;
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
    Math.sin(dLon / 2) * Math.sin(dLon / 2);
  return Math.round(2 * R * Math.asin(Math.sqrt(a)));
}
function fmt(n) { return String(Math.round(n)).replace(/\B(?=(\d{3})+(?!\d))/g, ","); }

const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const title = widget.addText("🛰 ISSはいまここ");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#7cc7f0");

widget.addSpacer(8);

try {
  const res = await fetch("https://api.wheretheiss.at/v1/satellites/25544");
  const d = await res.json();
  const lat = d.latitude, lon = d.longitude;
  const latS = (lat >= 0 ? "北緯" : "南緯") + " " + Math.abs(lat).toFixed(1) + "°";
  const lonS = (lon >= 0 ? "東経" : "西経") + " " + Math.abs(lon).toFixed(1) + "°";
  const pos = widget.addText(latS + " ／ " + lonS);
  pos.font = Font.boldSystemFont(17);
  pos.textColor = new Color("#ffffff");

  const meta = widget.addText(
    "高度 " + fmt(d.altitude) + "km ・ 速度 " + fmt(d.velocity) + "km/h" +
    (d.visibility === "eclipsed" ? " ・ 地球の影の中" : " ・ 太陽光の中")
  );
  meta.font = Font.systemFont(11);
  meta.textColor = new Color("#9ca3af");

  widget.addSpacer(8);
  // 経度バンド: 横軸が経度、縦軸が緯度の小さな地図。大阪の位置とISSの位置を点で打つ
  const W = 232, H = 52;
  const cv = widget.addCanvas(W, H);
  cv.rect(0, 0, W, H, "#0b1220");
  for (let g = -120; g <= 120; g += 60) {
    const gx = (g + 180) / 360 * W;
    cv.line(gx, 0, gx, H, "#1f2937", 1);
  }
  cv.line(0, H / 2, W, H / 2, "#1f2937", 1);
  const ox = (OSAKA.lon + 180) / 360 * W;
  const oy = (90 - OSAKA.lat) / 180 * H;
  cv.circle(ox, oy, 3, "#f78fb8");
  const ix = (lon + 180) / 360 * W;
  const iy = (90 - lat) / 180 * H;
  cv.circle(ix, iy, 4, "#facc15");
  cv.text(Math.min(ix + 6, W - 26), Math.max(iy - 5, 0), "ISS", 9, "#facc15", true);
  cv.text(Math.max(ox - 24, 0), Math.min(oy + 4, H - 10), "大阪", 9, "#f78fb8", false);

  widget.addSpacer(6);
  const dk = distKm(OSAKA.lat, OSAKA.lon, lat, lon);
  const near = widget.addText("大阪の地表から約 " + fmt(dk) + " km の地点の上空");
  near.font = Font.systemFont(12);
  near.textColor = new Color("#e0f2fb");
} catch (e) {
  const err = widget.addText("取得に失敗: " + e.message);
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}

Script.setWidget(widget);
""".trimIndent(),
        "🌙 今夜の月" to """
// プリセット: 今夜の月（月齢は天文計算、ネット不要）
// 満ち欠けはCanvasに1マスずつ判定して描く
const SYNODIC = 29.530588853;
const EPOCH = Date.UTC(2000, 0, 6, 18, 14); // 既知の新月
const now = new Date();
let age = ((now.getTime() - EPOCH) / 86400000) % SYNODIC;
if (age < 0) age += SYNODIC;
const t = age / SYNODIC; // 0=新月, 0.5=満月
const illum = (1 - Math.cos(2 * Math.PI * t)) / 2;

function phaseName(a) {
  if (a < 1.0 || a > 28.5) return "新月";
  if (a < 6.4) return "三日月";
  if (a < 8.4) return "上弦の月";
  if (a < 13.8) return "十三夜あたり";
  if (a < 16.2) return "満月";
  if (a < 21.1) return "寝待月あたり";
  if (a < 23.1) return "下弦の月";
  return "有明月";
}
const full = t <= 0.5 ? (SYNODIC / 2 - age) : (SYNODIC * 1.5 - age);
const newIn = SYNODIC - age;

const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const row = widget.addStack();
row.layoutHorizontally();

const S = 108, C = S / 2, R = 46;
const cv = row.addCanvas(S, S);
// 星空（月ごとに変わる配置）
let seed = now.getFullYear() * 100 + now.getMonth();
function rnd() { seed = (seed * 1103515245 + 12345) % 2147483648; return seed / 2147483648; }
for (let i = 0; i < 22; i++) {
  cv.circle(rnd() * S, rnd() * S, rnd() < 0.3 ? 1.4 : 0.8, "#4b5563");
}
cv.circle(C, C, R, "#2b3444");
// 光っているマスを1つずつ判定して描く（昼光の境界＝楕円の式）
const cosP = Math.cos(2 * Math.PI * t);
const step = 3;
for (let gx = -R; gx <= R; gx += step) {
  for (let gy = -R; gy <= R; gy += step) {
    if (gx * gx + gy * gy > R * R) continue;
    const edge = cosP * Math.sqrt(Math.max(0, R * R - gy * gy));
    const lit = t <= 0.5 ? (gx > edge) : (gx < -edge);
    if (lit) cv.rect(C + gx - step / 2, C + gy - step / 2, step, step, "#fef9c3");
  }
}

const side = row.addStack();
side.setPadding(8, 14, 0, 0);
const t1 = side.addText("🌙 今夜の月");
t1.font = Font.boldSystemFont(15);
t1.textColor = new Color("#f78fb8");
const t2 = side.addText(phaseName(age));
t2.font = Font.boldSystemFont(20);
t2.textColor = new Color("#ffffff");
const t3 = side.addText("月齢 " + age.toFixed(1) + " ・ 輝き " + Math.round(illum * 100) + "%");
t3.font = Font.systemFont(12);
t3.textColor = new Color("#9ca3af");
const t4 = side.addText("満月まで " + full.toFixed(0) + "日 ／ 新月まで " + newIn.toFixed(0) + "日");
t4.font = Font.systemFont(12);
t4.textColor = new Color("#9ca3af");

Script.setWidget(widget);
""".trimIndent(),
        "📌 あれから○日" to """
// プリセット: あれから○日（Storageで開始日を覚える）
// 初回実行日に「今日」を記録し、経過日数を数え続ける。
// 出来事の名前を変えたいときは下の EVENT を書き換えてください。
// 開始日を指定したいときは START に "2026-01-01" の形で書いてください（空なら初回実行日）。
const EVENT = "これを使い始めて";
const START = "";

function todayStr(d) {
  return d.getFullYear() + "-" + String(d.getMonth() + 1).padStart(2, "0") + "-" + String(d.getDate()).padStart(2, "0");
}
function dayDiff(a, b) {
  const da = new Date(a.getFullYear(), a.getMonth(), a.getDate());
  const db = new Date(b.getFullYear(), b.getMonth(), b.getDate());
  return Math.round((db - da) / 86400000);
}

const now = new Date();
let startStr = START;
if (!startStr) {
  startStr = Storage.get("start_date");
  if (!startStr) {
    startStr = todayStr(now);
    Storage.set("start_date", startStr);
  }
}
const parts = startStr.split("-");
const startDate = new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
const days = Math.max(0, dayDiff(startDate, now));

const milestones = [7, 30, 100, 200, 300, 365, 500, 730, 1000, 1500, 2000, 3000, 5000];
let next = null;
for (const m of milestones) { if (days < m) { next = m; break; } }
const yearFrac = Math.min(1, days / 365);

const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const t1 = widget.addText("📌 " + EVENT);
t1.font = Font.systemFont(13);
t1.textColor = new Color("#9ca3af");

const row = widget.addStack();
row.layoutHorizontally();
row.alignItems = "center";
const num = row.addText(String(days));
num.font = Font.boldSystemFont(48);
num.textColor = new Color("#ffffff");
const unit = row.addText(" 日");
unit.font = Font.boldSystemFont(20);
unit.textColor = new Color("#f78fb8");

const since = widget.addText(startStr.replace(/-/g, "/") + " から");
since.font = Font.systemFont(11);
since.textColor = new Color("#6b7280");

widget.addSpacer(10);
const barHolder = widget.addStack();
barHolder.setSize(232, 10);
barHolder.addProgress(yearFrac, new Color("#c2185b"), new Color("#374151"));
widget.addSpacer(6);
const note = widget.addText(
  next !== null
    ? "1年まで " + Math.round(yearFrac * 100) + "% ・ 次の節目 " + next + "日まであと " + (next - days) + "日"
    : "1年まで " + Math.round(yearFrac * 100) + "%"
);
note.font = Font.systemFont(12);
note.textColor = new Color("#cbd5e1");

Script.setWidget(widget);
""".trimIndent(),
        "🎨 日替わりアート" to """
// プリセット: 日替わりアート（日付シードのジェネラティブアート、ネット不要）
// 同じ日なら何度更新しても同じ絵。日付が変わると新しい絵になる
function mulberry32(a) {
  return function () {
    a |= 0; a = a + 0x6D2B79F5 | 0;
    let x = Math.imul(a ^ a >>> 15, 1 | a);
    x = x + Math.imul(x ^ x >>> 7, 61 | x) ^ x;
    return ((x ^ x >>> 14) >>> 0) / 4294967296;
  };
}
const now = new Date();
const seedNum = now.getFullYear() * 10000 + (now.getMonth() + 1) * 100 + now.getDate();
const rnd = mulberry32(seedNum);
const palette = ["#f78fb8", "#7cc7f0", "#ce9be8", "#ffab7a", "#facc15", "#ffffff"];
function pick() { return palette[Math.floor(rnd() * palette.length)]; }

const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const head = widget.addStack();
head.layoutHorizontally();
head.alignItems = "center";
const title = head.addText("🎨 日替わりアート");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
head.addSpacer();
const dt = head.addText((now.getMonth() + 1) + "/" + now.getDate());
dt.font = Font.systemFont(11);
dt.textColor = new Color("#9ca3af");

widget.addSpacer(8);
const W = 232, H = 118;
const cv = widget.addCanvas(W, H);
cv.rect(0, 0, W, H, "#0b1220");
// 大きな円を2つ（画面の重心）
for (let i = 0; i < 2; i++) {
  cv.circle(30 + rnd() * (W - 60), 24 + rnd() * (H - 48), 16 + rnd() * 26, pick());
}
// 中くらいの円とリング
const n = 5 + Math.floor(rnd() * 4);
for (let i = 0; i < n; i++) {
  const x = rnd() * W, y = rnd() * H, r = 4 + rnd() * 13;
  if (rnd() < 0.45) cv.ring(x, y, r, 2.5, 0, 360, pick());
  else cv.circle(x, y, r, pick());
}
// 斜めの線でリズムを作る
const lines = 2 + Math.floor(rnd() * 3);
for (let i = 0; i < lines; i++) {
  cv.line(rnd() * W, rnd() * H, rnd() * W, rnd() * H, pick(), 2);
}
// 小さな点の群れ
for (let i = 0; i < 14; i++) {
  cv.circle(rnd() * W, rnd() * H, 1.2, "#e5e7eb");
}

const cap = widget.addText("種（シード）は日付。明日はまた違う絵になります");
cap.font = Font.systemFont(10);
cap.textColor = new Color("#6b7280");

Script.setWidget(widget);
""".trimIndent(),
        "🌍 世界時計" to """
// プリセット: 世界時計（夏時間の開始・終了も自動判定、ネット不要）
// ルール: 米国=3月第2日曜〜11月第1日曜、欧州=3月最終日曜〜10月最終日曜
function nthSunday(y, m, n) {
  const first = new Date(Date.UTC(y, m, 1));
  return 1 + ((7 - first.getUTCDay()) % 7) + (n - 1) * 7;
}
function lastSunday(y, m) {
  const last = new Date(Date.UTC(y, m + 1, 0));
  return last.getUTCDate() - last.getUTCDay();
}
function isUsDst(t) {
  const y = new Date(t).getUTCFullYear();
  const start = Date.UTC(y, 2, nthSunday(y, 2, 2), 7, 0);
  const end = Date.UTC(y, 10, nthSunday(y, 10, 1), 6, 0);
  return t >= start && t < end;
}
function isEuDst(t) {
  const y = new Date(t).getUTCFullYear();
  const start = Date.UTC(y, 2, lastSunday(y, 2), 1, 0);
  const end = Date.UTC(y, 9, lastSunday(y, 9), 1, 0);
  return t >= start && t < end;
}
const pad2 = function (n) { return String(n).padStart(2, "0"); };
function fmtAt(utcMs, offsetMin, baseDate) {
  const d = new Date(utcMs + offsetMin * 60000);
  const dayDiff = Math.round(
    (Date.UTC(d.getUTCFullYear(), d.getUTCMonth(), d.getUTCDate()) -
      Date.UTC(baseDate.getUTCFullYear(), baseDate.getUTCMonth(), baseDate.getUTCDate())) / 86400000
  );
  const suffix = dayDiff > 0 ? "（翌日）" : dayDiff < 0 ? "（前日）" : "";
  return pad2(d.getUTCHours()) + ":" + pad2(d.getUTCMinutes()) + suffix;
}

const nowMs = Date.now();
const tokyoNow = new Date(nowMs + 9 * 3600000);
const us = isUsDst(nowMs), eu = isEuDst(nowMs);
const cities = [
  ["東京", 540],
  ["ロンドン", eu ? 60 : 0],
  ["パリ", eu ? 120 : 60],
  ["ニューヨーク", us ? -240 : -300],
  ["ロサンゼルス", us ? -420 : -480],
];

const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const title = widget.addText("🌍 世界時計");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#7cc7f0");
widget.addSpacer(8);

cities.forEach(function (c, i) {
  if (i > 0) widget.addSpacer(5);
  const row = widget.addStack();
  row.layoutHorizontally();
  row.alignItems = "center";
  const name = row.addText(c[0]);
  name.font = Font.systemFont(13);
  name.textColor = new Color("#9ca3af");
  row.addSpacer();
  const time = row.addText(fmtAt(nowMs, c[1], tokyoNow));
  time.font = Font.boldSystemFont(17);
  time.textColor = new Color(i === 0 ? "#f78fb8" : "#ffffff");
});

Script.setWidget(widget);
""".trimIndent(),
        "🗓 月間カレンダー" to """
// プリセット: 月間カレンダー（Canvasで今月を描く、ネット不要）
// 今日はピンクの丸で囲む。日曜はピンク、土曜は水色
const now = new Date();
const y = now.getFullYear(), m = now.getMonth();
const firstDow = new Date(y, m, 1).getDay();
const daysInMonth = new Date(y, m + 1, 0).getDate();
const weeks = Math.ceil((firstDow + daysInMonth) / 7);

const widget = new ListWidget();
widget.backgroundColor = new Color("#ffffff");
widget.setPadding(12, 12, 12, 12);

const head = widget.addStack();
head.layoutHorizontally();
head.alignItems = "center";
const title = head.addText(y + "年" + (m + 1) + "月");
title.font = Font.boldSystemFont(16);
title.textColor = new Color("#c2185b");
head.addSpacer();
const left = head.addText("あと" + (daysInMonth - now.getDate()) + "日で来月");
left.font = Font.systemFont(11);
left.textColor = new Color("#6b7280");

widget.addSpacer(6);
const W = 238, colW = W / 7, headH = 15, rowH = 23;
const H = headH + weeks * rowH;
const cv = widget.addCanvas(W, H);
const dowNames = ["日", "月", "火", "水", "木", "金", "土"];
for (let c = 0; c < 7; c++) {
  const color = c === 0 ? "#c2185b" : c === 6 ? "#0277bd" : "#6b7280";
  const label = dowNames[c];
  cv.text(c * colW + colW / 2 - 4, 0, label, 10, color, true);
}
for (let d = 1; d <= daysInMonth; d++) {
  const idx = firstDow + d - 1;
  const col = idx % 7;
  const rowIdx = Math.floor(idx / 7);
  const cx = col * colW + colW / 2;
  const cy = headH + rowIdx * rowH + rowH / 2;
  const isToday = d === now.getDate();
  if (isToday) cv.circle(cx, cy, 10, "#c2185b");
  const color = isToday ? "#ffffff" : col === 0 ? "#c2185b" : col === 6 ? "#0277bd" : "#111827";
  const s = String(d);
  cv.text(cx - (s.length > 1 ? 6 : 3), cy - 6, s, 12, color, isToday);
}

Script.setWidget(widget);
""".trimIndent(),
        "📊 時間の進み具合" to """
// プリセット: 時間の進み具合（今日・今週・今月・今年の経過率、ネット不要）
const now = new Date();
const startOfDay = new Date(now.getFullYear(), now.getMonth(), now.getDate());
const dayFrac = (now - startOfDay) / 86400000;
const weekFrac = (now.getDay() + dayFrac) / 7;
const daysInMonth = new Date(now.getFullYear(), now.getMonth() + 1, 0).getDate();
const monthFrac = (now.getDate() - 1 + dayFrac) / daysInMonth;
const yearStart = new Date(now.getFullYear(), 0, 1);
const yearEnd = new Date(now.getFullYear() + 1, 0, 1);
const yearFrac = (now - yearStart) / (yearEnd - yearStart);

const rows = [
  ["今日", dayFrac, "#f78fb8"],
  ["今週", weekFrac, "#ce9be8"],
  ["今月", monthFrac, "#7cc7f0"],
  ["今年", yearFrac, "#ffab7a"],
];

const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const title = widget.addText("📊 時間の進み具合");
title.font = Font.boldSystemFont(15);
title.textColor = new Color("#ffffff");
widget.addSpacer(10);

rows.forEach(function (r, i) {
  if (i > 0) widget.addSpacer(8);
  const head = widget.addStack();
  head.layoutHorizontally();
  const lab = head.addText(r[0]);
  lab.font = Font.systemFont(12);
  lab.textColor = new Color("#9ca3af");
  head.addSpacer();
  const pct = head.addText(Math.floor(r[1] * 100) + "%");
  pct.font = Font.boldSystemFont(12);
  pct.textColor = new Color(r[2]);
  const holder = widget.addStack();
  holder.setSize(232, 9);
  holder.addProgress(r[1], new Color(r[2]), new Color("#374151"));
});

Script.setWidget(widget);
""".trimIndent(),
        "✨ おしゃれ時計（ダーク）" to """
// プリセット: おしゃれ時計（ダーク）— 大きな時刻と「今日の経過」リング
const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const now = new Date();
const pad = function (n) { return String(n).padStart(2, "0"); };
const dow = "日月火水木金土"[now.getDay()];
const dayFrac = (now.getHours() * 60 + now.getMinutes()) / 1440;

const row = widget.addStack();
row.layoutHorizontally();
row.alignItems = "center";

const cv = row.addCanvas(84, 84);
cv.ring(42, 42, 34, 8, -90, 360, "#374151");
cv.ring(42, 42, 34, 8, -90, 360 * dayFrac, "#f78fb8");
cv.text(27, 35, Math.round(dayFrac * 100) + "%", 15, "#ffffff", true);

const side = row.addStack();
side.setPadding(2, 14, 0, 0);
const time = side.addText(pad(now.getHours()) + ":" + pad(now.getMinutes()));
time.font = Font.boldSystemFont(38);
time.textColor = new Color("#ffffff");
const date = side.addText((now.getMonth() + 1) + "月" + now.getDate() + "日（" + dow + "）");
date.font = Font.systemFont(13);
date.textColor = new Color("#9ca3af");
const cap = side.addText("今日はあと " + (24 - now.getHours()) + "時間ほど");
cap.font = Font.systemFont(11);
cap.textColor = new Color("#f78fb8");

Script.setWidget(widget);
""".trimIndent(),
        "⏳ 年末カウントダウン" to """
// プリセット: 年末カウントダウン（Canvasの円ゲージ＋大きな数字）
const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const now = new Date();
const startOfDay = new Date(now.getFullYear(), now.getMonth(), now.getDate());
const yearStart = new Date(now.getFullYear(), 0, 1);
const yearEnd = new Date(now.getFullYear(), 11, 31);
const dayOfYear = Math.floor((startOfDay - yearStart) / 86400000) + 1;
const daysInYear = Math.floor((yearEnd - yearStart) / 86400000) + 1;
const left = daysInYear - dayOfYear;
const frac = dayOfYear / daysInYear;
const weeks = Math.floor(left / 7);
const extraDays = left % 7;

const row = widget.addStack();
row.layoutHorizontally();

const cv = row.addCanvas(96, 96);
cv.ring(48, 48, 40, 10, -90, 360, "#374151");
cv.ring(48, 48, 40, 10, -90, 360 * frac, "#f78fb8");
cv.text(30, 40, Math.round(frac * 100) + "%", 17, "#ffffff", true);

const side = row.addStack();
side.setPadding(10, 14, 0, 0);
const t1 = side.addText(now.getFullYear() + "年もあと");
t1.font = Font.systemFont(13);
t1.textColor = new Color("#9ca3af");
const t2 = side.addText(left + "日");
t2.font = Font.boldSystemFont(38);
t2.textColor = new Color("#ffffff");
const t3 = side.addText("＝ " + weeks + "週間と" + extraDays + "日 ／ " + (left * 24) + "時間");
t3.font = Font.systemFont(12);
t3.textColor = new Color("#f78fb8");
const t4 = side.addText("今日は " + dayOfYear + " 日目");
t4.font = Font.systemFont(12);
t4.textColor = new Color("#9ca3af");

Script.setWidget(widget);
""".trimIndent(),
        "Hello＋現在時刻" to """
// Scriptlet サンプル: Hello + 現在時刻（いちばん小さな例）
// ウィジェットは「更新された時点」の時刻を表示します（Androidの仕様で定期更新は最短30分）
const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const title = widget.addText("Hello, Scriptlet!");
title.font = Font.boldSystemFont(20);
title.textColor = new Color("#ffffff");

widget.addSpacer(6);

const now = new Date();
const pad = function (n) { return String(n).padStart(2, "0"); };
const time = widget.addText("いま " + pad(now.getHours()) + ":" + pad(now.getMinutes()));
time.font = Font.systemFont(14);
time.textColor = new Color("#f78fb8");

const dow = "日月火水木金土"[now.getDay()];
const date = widget.addText((now.getMonth() + 1) + "月" + now.getDate() + "日（" + dow + "）");
date.font = Font.systemFont(12);
date.textColor = new Color("#9ca3af");

Script.setWidget(widget);
""".trimIndent(),
    )
}
