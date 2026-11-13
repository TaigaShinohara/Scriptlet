package app.scriptlet.engine

object SampleScripts {
    /** Stable keys, index-aligned with [all]; used to seed new samples on upgrade without resurrecting deleted ones. */
    val keys: List<String> = listOf(
        "hello", "weekly-calendar", "fetch", "stylish-clock", "canvas-pie", "progress-demo",
        "showcase-weather", "showcase-countdown", "showcase-week",
    )

    /** One-line descriptions, index-aligned with [keys]/[all]; shown wherever a script is picked. */
    val descs: List<String> = listOf(
        "文字と現在時刻を出す、いちばん小さな例",
        "今週の予定を曜日ごとに並べる表",
        "ネットからデータを取得して表示する例",
        "ダーク背景の大きな時計",
        "Canvasで円グラフを描く例",
        "進捗バーで達成率を見せる例",
        "大阪の天気と気温グラフ（本物のデータ）",
        "今年の残り日数を円ゲージで表示",
        "今週の予定と今日のタスクのまとめ",
    )

    /** The showcase presets: full real-world widgets, offered first when creating. */
    val featuredKeys: Set<String> = setOf("showcase-weather", "showcase-countdown", "showcase-week")

    fun descForName(name: String): String? {
        val idx = all.indexOfFirst { it.first == name }
        return if (idx >= 0) descs.getOrNull(idx) else null
    }

    val all: List<Pair<String, String>> = listOf(
        "Hello＋現在時刻" to """
// Scriptlet サンプル1: Hello + 現在時刻
// ウィジェットは「更新された時点」の時刻を表示します（Androidの仕様で定期更新は最短30分）
const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const title = widget.addText("Hello, Scriptlet!");
title.font = Font.boldSystemFont(20);
title.textColor = new Color("#ffffff");

widget.addSpacer(6);

const now = new Date();
const time = widget.addText("いま " + now.toLocaleTimeString("ja-JP"));
time.font = Font.systemFont(14);
time.textColor = new Color("#f78fb8");

const date = widget.addText(now.toLocaleDateString("ja-JP", { weekday: "long", month: "long", day: "numeric" }));
date.font = Font.systemFont(12);
date.textColor = new Color("#9ca3af");

Script.setWidget(widget);
""".trimIndent(),
        "週間カレンダー" to """
// Scriptlet サンプル2: 週間カレンダー（今週の日付を自動生成、予定はサンプルデータ）
const widget = new ListWidget();
widget.backgroundColor = new Color("#ffffff");
widget.setPadding(12, 12, 12, 12);

const title = widget.addText("今週");
title.font = Font.boldSystemFont(16);
title.textColor = new Color("#c2185b");

widget.addSpacer(8);

const sampleEvents = { "月": ["筋トレ"], "水": ["乗馬"], "金": ["乗馬", "サウナ"], "日": ["おでかけ"] };
const dowNames = ["日", "月", "火", "水", "木", "金", "土"];
const today = new Date();

const row = widget.addStack();
row.layoutHorizontally();
row.spacing = 6;

for (let i = 0; i < 7; i++) {
  const d = new Date(today.getFullYear(), today.getMonth(), today.getDate() - today.getDay() + i);
  const dow = dowNames[d.getDay()];
  const isToday = d.getDate() === today.getDate();
  const hasEvent = (sampleEvents[dow] || []).length > 0;

  const cell = row.addStack();
  cell.layoutVertically();
  cell.setPadding(6, 4, 6, 4);
  cell.cornerRadius = 8;
  cell.backgroundColor = new Color(isToday ? "#c2185b" : (hasEvent ? "#fce7f1" : "#f3f4f6"));

  const dowText = cell.addText(dow);
  dowText.font = Font.systemFont(11);
  dowText.textColor = new Color(isToday ? "#ffffff" : "#6b7280");

  const dayText = cell.addText(String(d.getDate()));
  dayText.font = Font.boldSystemFont(15);
  dayText.textColor = new Color(isToday ? "#ffffff" : "#111827");

  const evText = cell.addText(hasEvent ? sampleEvents[dow][0] : "　");
  evText.font = Font.systemFont(9);
  evText.textColor = new Color(isToday ? "#ffffff" : "#c2185b");
}

Script.setWidget(widget);
""".trimIndent(),
        "Fetchサンプル" to """
// Scriptlet サンプル3: fetchでJSONを取って表示（ネット必須）
const widget = new ListWidget();
widget.backgroundColor = new Color("#1d3242");
widget.setPadding(14, 14, 14, 14);

const title = widget.addText("Fetchサンプル");
title.font = Font.boldSystemFont(16);
title.textColor = new Color("#7cc7f0");

widget.addSpacer(6);

try {
  const res = await fetch("https://jsonplaceholder.typicode.com/todos/1");
  const data = await res.json();
  const body = widget.addText("取得OK: " + data.title);
  body.font = Font.systemFont(13);
  body.textColor = new Color("#ffffff");
  console.log("fetched id=" + data.id);
} catch (e) {
  const body = widget.addText("取得に失敗: " + e.message);
  body.font = Font.systemFont(13);
  body.textColor = new Color("#ffab7a");
}

Script.setWidget(widget);
""".trimIndent(),
        "おしゃれ時計（ダーク）" to """
// Scriptlet サンプル4: ダークでおしゃれな時計（Phase 3の新しい部品: 区切り線・可変スペーサー・整列・セグメントバー）
const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(16, 16, 16, 16);

const now = new Date();
const pad = function (n) { return String(n).padStart(2, "0"); };
const dow = "日月火水木金土"[now.getDay()];

// ヘッダー行: 左にラベル、右に日付（間の可変スペーサーで両端に分かれる）
const head = widget.addStack();
head.layoutHorizontally();
head.alignItems = "center";
const lab = head.addText(Icons.moon + " Scriptlet");
lab.font = Font.boldSystemFont(12);
lab.textColor = new Color("#f78fb8");
head.addSpacer();
const dt = head.addText((now.getMonth() + 1) + "/" + now.getDate() + "（" + dow + "）");
dt.font = Font.systemFont(12);
dt.textColor = new Color("#9ca3af");

widget.addSpacer(8);

const time = widget.addText(pad(now.getHours()) + ":" + pad(now.getMinutes()));
time.font = Font.boldSystemFont(40);
time.textColor = new Color("#ffffff");

widget.addSpacer(10);
widget.addDivider(new Color("#374151"));
widget.addSpacer(10);

// 1日の経過をセグメントバーで表示
const frac = (now.getHours() * 60 + now.getMinutes()) / 1440;
const SEG = 16;
const filled = Math.round(frac * SEG);
const bar = widget.addStack();
bar.layoutHorizontally();
bar.spacing = 3;
for (let i = 0; i < SEG; i++) {
  const s = bar.addStack();
  s.setSize(12, 6);
  s.cornerRadius = 3;
  s.backgroundColor = new Color(i < filled ? "#f78fb8" : "#374151");
}

widget.addSpacer(6);

const cap = widget.addText("今日の経過 " + Math.round(frac * 100) + "%");
cap.font = Font.systemFont(11);
cap.textColor = new Color("#9ca3af");

Script.setWidget(widget);
""".trimIndent(),
        "円グラフ（Canvas）" to """
// Scriptlet サンプル5: Canvasで円グラフを描く（v0.5の新部品）
const widget = new ListWidget();
widget.backgroundColor = new Color("#ffffff");
widget.setPadding(14, 14, 14, 14);

const title = widget.addText("📊 今日の時間の内訳");
title.font = Font.boldSystemFont(15);
title.textColor = new Color("#111827");

widget.addSpacer(8);

const row = widget.addStack();
row.layoutHorizontally();

const cv = row.addCanvas(110, 110);
PieChart(cv, 55, 55, 52, [
  [5, "#c2185b"],   // 制作
  [3, "#6a1b9a"],   // 勉強
  [2, "#0277bd"],   // 運動
  [4, "#e5e7eb"],   // その他
]);
cv.circle(55, 55, 30, "#ffffff");
cv.text(38, 48, "14h", 15, "#111827", true);

const legend = row.addStack();
legend.setPadding(10, 10, 0, 0);
const items = [["制作 5h", "#c2185b"], ["勉強 3h", "#6a1b9a"], ["運動 2h", "#0277bd"], ["その他 4h", "#9ca3af"]];
for (const [label, color] of items) {
  const t = legend.addText("■ " + label);
  t.font = Font.systemFont(12);
  t.textColor = new Color(color);
  legend.addSpacer(4);
}

Script.setWidget(widget);
""".trimIndent(),
        "達成率メーター" to """
// Scriptlet サンプル6: プログレスバーとゲージ（v0.5の新部品）
const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const title = widget.addText("🎯 今日の達成率");
title.font = Font.boldSystemFont(15);
title.textColor = new Color("#ffffff");

widget.addSpacer(8);

const row = widget.addStack();
row.layoutHorizontally();

const cv = row.addCanvas(84, 84);
const pct = 0.68;
cv.ring(42, 42, 34, 9, -90, 360, "#374151");
cv.ring(42, 42, 34, 9, -90, 360 * pct, "#f78fb8");
cv.text(26, 34, Math.round(pct * 100) + "%", 16, "#ffffff", true);

const side = row.addStack();
side.setPadding(6, 12, 0, 0);
const goals = [["筋トレ", 1.0, "#c2185b"], ["勉強 30分", 0.6, "#6a1b9a"], ["散歩", 0.35, "#0277bd"]];
for (const [label, v, color] of goals) {
  const t = side.addText(label + " " + Math.round(v * 100) + "%");
  t.font = Font.systemFont(12);
  t.textColor = new Color("#ffffff");
  const barHolder = side.addStack();
  barHolder.setSize(120, 10);
  barHolder.addProgress(v, new Color(color), new Color("#374151"));
  side.addSpacer(6);
}

widget.addSpacer(4);
const link = widget.addLink("🔗 Scriptletのページを開く", "https://example.com");
link.font = Font.systemFont(11);

Script.setWidget(widget);
""".trimIndent(),
        "🌤 大阪の天気（実データ）" to """
// プリセット: 大阪の天気（Open-Meteo、APIキー不要の実データ取得）
// fetch・Canvas棒グラフ・大きな数字の組み合わせ
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

const widget = new ListWidget();
widget.backgroundColor = new Color("#1d3242");
widget.setPadding(14, 14, 14, 14);

const head = widget.addStack();
head.layoutHorizontally();
head.alignItems = "center";
const title = head.addText("🌤 大阪の天気");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#7cc7f0");
head.addSpacer();
const now = new Date();
const upd = head.addText("更新 " + now.getHours() + ":" + String(now.getMinutes()).padStart(2, "0"));
upd.font = Font.systemFont(10);
upd.textColor = new Color("#9ca3af");

widget.addSpacer(8);

try {
  const res = await fetch("https://api.open-meteo.com/v1/forecast?latitude=34.6937&longitude=135.5023&current=temperature_2m,weather_code&daily=temperature_2m_max,temperature_2m_min&hourly=temperature_2m&forecast_days=1&timezone=Asia%2FTokyo");
  const d = await res.json();
  const [emoji, label] = codeInfo(d.current.weather_code);

  const big = widget.addStack();
  big.layoutHorizontally();
  big.alignItems = "center";
  const em = big.addText(emoji);
  em.font = Font.systemFont(30);
  const temp = big.addText(" " + Math.round(d.current.temperature_2m) + "°");
  temp.font = Font.boldSystemFont(34);
  temp.textColor = new Color("#ffffff");
  const lab = big.addText(" " + label);
  lab.font = Font.systemFont(14);
  lab.textColor = new Color("#e0f2fb");

  const hl = widget.addText("最高 " + Math.round(d.daily.temperature_2m_max[0]) + "° ／ 最低 " + Math.round(d.daily.temperature_2m_min[0]) + "°");
  hl.font = Font.systemFont(12);
  hl.textColor = new Color("#9ca3af");

  widget.addSpacer(8);
  const temps = [];
  for (let h = 0; h < 24; h += 2) temps.push(d.hourly.temperature_2m[h]);
  const cv = widget.addCanvas(232, 52);
  BarChart(cv, 0, 0, 232, 52, temps, Math.max.apply(null, temps) + 2, "#7cc7f0", null);
  const cap = widget.addText("0時から2時間ごとの気温");
  cap.font = Font.systemFont(10);
  cap.textColor = new Color("#9ca3af");
} catch (e) {
  const err = widget.addText("取得に失敗: " + e.message);
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}

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
const t3 = side.addText("今日は " + dayOfYear + " 日目");
t3.font = Font.systemFont(12);
t3.textColor = new Color("#f78fb8");

Script.setWidget(widget);
""".trimIndent(),
        "🗓 週間ダッシュボード" to """
// プリセット: 全部入りダッシュボード（時計・週間・今日の経過バー・リンク）
const widget = new ListWidget();
widget.backgroundColor = new Color("#111827");
widget.setPadding(14, 14, 14, 14);

const now = new Date();
const pad = function (n) { return String(n).padStart(2, "0"); };
const dowNames = ["日", "月", "火", "水", "木", "金", "土"];

const head = widget.addStack();
head.layoutHorizontally();
head.alignItems = "center";
const time = head.addText(pad(now.getHours()) + ":" + pad(now.getMinutes()));
time.font = Font.boldSystemFont(26);
time.textColor = new Color("#ffffff");
head.addSpacer();
const dt = head.addText((now.getMonth() + 1) + "/" + now.getDate() + "（" + dowNames[now.getDay()] + "）");
dt.font = Font.systemFont(13);
dt.textColor = new Color("#9ca3af");

widget.addSpacer(8);

const week = widget.addStack();
week.layoutHorizontally();
week.spacing = 5;
for (let i = 0; i < 7; i++) {
  const d = new Date(now.getFullYear(), now.getMonth(), now.getDate() - now.getDay() + i);
  const isToday = d.getDate() === now.getDate();
  const cell = week.addStack();
  cell.setPadding(5, 7, 5, 7);
  cell.cornerRadius = 8;
  cell.backgroundColor = new Color(isToday ? "#c2185b" : "#1f2937");
  const a = cell.addText(dowNames[d.getDay()]);
  a.font = Font.systemFont(10);
  a.textColor = new Color(isToday ? "#ffffff" : "#9ca3af");
  const b = cell.addText(String(d.getDate()));
  b.font = Font.boldSystemFont(14);
  b.textColor = new Color("#ffffff");
}

widget.addSpacer(10);
widget.addDivider(new Color("#374151"));
widget.addSpacer(8);

const frac = (now.getHours() * 60 + now.getMinutes()) / 1440;
const capRow = widget.addStack();
capRow.layoutHorizontally();
const cap = capRow.addText("今日の経過");
cap.font = Font.systemFont(11);
cap.textColor = new Color("#9ca3af");
capRow.addSpacer();
const pct = capRow.addText(Math.round(frac * 100) + "%");
pct.font = Font.boldSystemFont(11);
pct.textColor = new Color("#f78fb8");
const barHolder = widget.addStack();
barHolder.setSize(232, 10);
barHolder.addProgress(frac, new Color("#c2185b"), new Color("#374151"));

widget.addSpacer(8);
const link = widget.addLink("🔗 大阪の天気予報を開く", "https://tenki.jp/forecast/6/31/");
link.font = Font.systemFont(11);

Script.setWidget(widget);
""".trimIndent(),
    )
}
