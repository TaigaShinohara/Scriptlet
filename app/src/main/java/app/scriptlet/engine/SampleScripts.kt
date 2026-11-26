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
        "hello",
        "anim-bounce",
        "mandala",
        "pixel-art",
        "wave-scope",
        "fractal-tree",
        "orbit",
        "maze",
        "star-chart",
        "on-this-day",
        "umbrella",
        "laundry",
        "uv",
        "sunrise",
        "fx-quick",
        "trash-day",
        "next-holiday",
        "birthday",
        "savings",
        "water",
        "omikuji",
        "gacha",
        "level",
        "boss",
        "lunch",
        "unsei",
        "big-clock",
        "date-only",
        "week-progress",
        "one-word",
        "breathe",
        "life-days",
        "skyline",
        "sunset",
        "aurora",
        "grass",
        "face",
        "senryu",
        "qr-mock",
        "bitcoin",
        "gold",
        "spiral",
        "payday",
        "stretch",
        "english",
        "kanji",
        "warikan",
        "bmi",
        "reading",
        "discomfort",
        "cooking",
        "cleaning",
        "janken",
        "dice",
        "roulette",
        "meigen",
        "age-planet",
        "cat-word",
        "month-only",
        "day-left",
        "color-today",
        "greeting",
        "kaleido",
        "rain-window",
        "topo",
        "bubbles",
        "tile-pattern",
        "meds",
        "plant",
        "fridge",
        "sleep-debt",
        "eye-rest",
        "fortune-cookie",
        "lucky",
        "monster-egg",
        "tide",
        "quake-count",
        "era",
        "seconds-today",
        "proverb",
        "math-drill",
        "pref",
        "constellation",
        "coffee",
        "dream",
        "gratitude",
        "chord",
    )

    /** One-line descriptions, index-aligned with [keys]/[all]; shown wherever a script is picked. */
    val descs: List<String> = listOf(
        "東京の天気・気温曲線・週間予報まで凝縮",
        "日本の最新地震を震度つきで表示",
        "過去30営業日のドル円を折れ線グラフで",
        "Canvasで目盛りと針まで描くアナログ時計",
        "国際宇宙ステーションの現在位置と大阪からの距離",
        "月齢と満ち欠けを計算して描く（ネット不要）",
        "開始日を覚えて経過日数を数え続ける（Storage）",
        "日付で絵が変わるジェネラティブアート（Canvas）",
        "5都市の現在時刻。夏時間も自動で判定",
        "今月のカレンダーをCanvasで描く（ネット不要）",
        "今日・今週・今月・今年の経過率をバーで表示",
        "大きな時計と今日の経過リング",
        "三重リングと12月カレンダーで今年の残りを表示",
        "文字と現在時刻を出す、いちばん小さな例",
        "アニメ実験：跳ねるボール（アニメ専用ウィジェットで動く）",
        "日付で模様が変わる万華鏡アート（Canvas・ネット不要）",
        "左右対称のドット絵モンスターを日替わり生成（Canvas）",
        "重なる波が描くアート。更新するたび少し変化（Canvas）",
        "再帰で育つ木。枝ぶりは日替わり（Canvas）",
        "今日の惑星の位置を計算して描く（ネット不要）",
        "毎日新しい迷路を生成（Canvas・ネット不要）",
        "日替わりの星屑と想像の星座（Canvas）",
        "歴史の今日を1件（Wikipedia）",
        "今日の降水確率だけを大きく表示",
        "外干しできる？を天気から判定",
        "今日の紫外線の強さと対策",
        "日の出・日の入りと日の長さ",
        "ドル・ユーロ・ポンドの対円レート",
        "曜日で今日のゴミを判定（ルールは書き換え式）",
        "次の祝日まであと何日（2027年分まで収録）",
        "大切な日まであと何日（日付は書き換え式）",
        "目標までの貯金の進み具合（金額は書き換え式）",
        "時刻から今日の水分の目安を表示",
        "日替わり運勢とラッキーアイテム",
        "1日1回のレア引き気分（日替わり）",
        "今年の経過日数＝あなたのレベル",
        "週末までの距離をボス戦っぽく表示",
        "今日のランチを日替わりで決定",
        "12星座から今日いちばん運のいい星座",
        "時刻を超大きく表示する一番シンプルな時計",
        "月日と曜日だけを超大きく",
        "月〜日の7マスで今週の進み具合",
        "日替わりの短くてやさしい一文",
        "円を見ながら呼吸を整える",
        "生まれてから何日目か（誕生日は書き換え式）",
        "ビル群と窓明かりを日替わり生成（ネット不要）",
        "時刻で色が変わるグラデーション（ネット不要）",
        "波打つ光のカーテンを生成（ネット不要）",
        "GitHub風ヒートマップで今年の経過（ネット不要）",
        "目・口・髪を組み合わせる似顔絵生成（ネット不要）",
        "5・7・5を組み立てる自動川柳（ネット不要）",
        "QRっぽい模様。読み取れません（ネット不要）",
        "円建て価格と24時間の動き",
        "金連動トークン経由の価格",
        "1,1,2,3,5…の黄金螺旋（ネット不要）",
        "毎月25日まであと何日か（ネット不要）",
        "曜日ごとに部位が回るメニュー（ネット不要）",
        "例文つきで1日1語（ネット不要）",
        "読み・意味・成り立ちメモつき（ネット不要）",
        "合計と人数を書き換えて使う（ネット不要）",
        "身長と体重を書き換えて使う（ネット不要）",
        "読んだページを書き換えて使う（ネット不要）",
        "東京の気温と湿度から計算",
        "主菜＋副菜＋汁物を日替わり提案（ネット不要）",
        "1日1か所だけ提案（ネット不要）",
        "相手の出す手は決まっている（ネット不要）",
        "更新のたびに振り直し（ネット不要）",
        "名前リストを書き換えて使う（ネット不要）",
        "それっぽい格言の自動生成（ネット不要）",
        "誕生日を書き換え、惑星ごとの年齢（ネット不要）",
        "猫っぽい短文と顔文字（ネット不要）",
        "月を大きく＋残り日数（ネット不要）",
        "今日があと何%か（ネット不要）",
        "日替わりの伝統色と色コード（ネット不要）",
        "時間帯の挨拶と時刻だけ（ネット不要）",
        "日替わりの万華鏡アート",
        "雨粒のアート（日替わり）",
        "架空の山の等高線アート",
        "深海の泡アート",
        "タイル模様のアート",
        "次に飲む時刻と回数（時刻は書き換え式）",
        "次の水やり日までのカウント（書き換え式）",
        "中身のメモ（書き換え式）",
        "ふだんの就寝・起床から睡眠時間を表示",
        "20-20-20ルールのリマインダー",
        "日替わりのひとこと占い",
        "ラッキーナンバーとアイテム",
        "孵化までのカウントダウン",
        "月齢から計算する潮の目安",
        "今日の地震の回数と最大震度",
        "令和・干支・今年の何日目か",
        "今日の経過秒数と残り",
        "日替わりのことわざ",
        "日替わり3問の計算ドリル",
        "日替わりで都道府県と県庁所在地",
        "季節のおすすめ星座",
        "コーヒー豆のメモ（書き換え式）",
        "朝の夢メモのテンプレ",
        "日替わりの感謝テーマ",
        "日替わりのコード進行とキー",
    )

    /** The showcase presets: full real-world widgets, offered first when creating. */
    val featuredKeys: Set<String> = setOf("showcase-weather", "quake", "fx-chart", "analog-clock")

    /** Newest preset batch (2026-11-25 batch 3); the feed shows them right after the featured ones. */
    val freshKeys: Set<String> = setOf("kaleido", "rain-window", "topo", "bubbles", "tile-pattern", "meds", "plant", "fridge", "sleep-debt", "eye-rest", "fortune-cookie", "lucky", "monster-egg", "tide", "quake-count", "era", "seconds-today", "proverb", "math-drill", "pref", "constellation", "coffee", "dream", "gratitude", "chord")

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

        /** Genre tags per preset key (v0.26.0); the feed filters by these. */
    val tags: Map<String, List<String>> = mapOf(
        "showcase-weather" to listOf("📡 実データ"),
        "quake" to listOf("📡 実データ"),
        "fx-chart" to listOf("📡 実データ"),
        "analog-clock" to listOf("🕐 時計と暦"),
        "iss" to listOf("📡 実データ"),
        "moon" to listOf("🕐 時計と暦"),
        "days-since" to listOf("🕐 時計と暦"),
        "daily-art" to listOf("🎨 アート"),
        "world-clock" to listOf("🕐 時計と暦"),
        "weekly-calendar" to listOf("🕐 時計と暦"),
        "progress-demo" to listOf("🕐 時計と暦"),
        "stylish-clock" to listOf("🕐 時計と暦"),
        "showcase-countdown" to listOf("🕐 時計と暦"),
        "hello" to listOf("🎲 お遊び"),
        "anim-bounce" to listOf("🎨 アート"),
        "mandala" to listOf("🎨 アート"),
        "pixel-art" to listOf("🎨 アート"),
        "wave-scope" to listOf("🎨 アート"),
        "fractal-tree" to listOf("🎨 アート"),
        "orbit" to listOf("🕐 時計と暦"),
        "maze" to listOf("🎨 アート"),
        "star-chart" to listOf("🎨 アート"),
        "on-this-day" to listOf("📚 ことば・学び", "📡 実データ"),
        "umbrella" to listOf("📡 実データ", "🧺 生活"),
        "laundry" to listOf("📡 実データ", "🧺 生活"),
        "uv" to listOf("📡 実データ"),
        "sunrise" to listOf("📡 実データ", "🕐 時計と暦"),
        "fx-quick" to listOf("📡 実データ"),
        "trash-day" to listOf("🧺 生活"),
        "next-holiday" to listOf("🕐 時計と暦", "🧺 生活"),
        "birthday" to listOf("🧺 生活", "🕐 時計と暦"),
        "savings" to listOf("🧺 生活"),
        "water" to listOf("🧺 生活"),
        "omikuji" to listOf("🎲 お遊び"),
        "gacha" to listOf("🎲 お遊び"),
        "level" to listOf("🎲 お遊び"),
        "boss" to listOf("🎲 お遊び"),
        "lunch" to listOf("🎲 お遊び", "🧺 生活"),
        "unsei" to listOf("🎲 お遊び"),
        "big-clock" to listOf("🕐 時計と暦"),
        "date-only" to listOf("🕐 時計と暦"),
        "week-progress" to listOf("🕐 時計と暦"),
        "one-word" to listOf("📚 ことば・学び"),
        "breathe" to listOf("🧺 生活"),
        "life-days" to listOf("🕐 時計と暦", "🧺 生活"),
        "skyline" to listOf("🎨 アート"),
        "sunset" to listOf("🎨 アート"),
        "aurora" to listOf("🎨 アート"),
        "grass" to listOf("🎨 アート"),
        "face" to listOf("🎨 アート", "🎲 お遊び"),
        "senryu" to listOf("📚 ことば・学び", "🎲 お遊び"),
        "qr-mock" to listOf("🎨 アート", "🎲 お遊び"),
        "bitcoin" to listOf("📡 実データ"),
        "gold" to listOf("📡 実データ"),
        "spiral" to listOf("🎨 アート"),
        "payday" to listOf("🧺 生活", "🕐 時計と暦"),
        "stretch" to listOf("🧺 生活"),
        "english" to listOf("📚 ことば・学び"),
        "kanji" to listOf("📚 ことば・学び"),
        "warikan" to listOf("🧺 生活"),
        "bmi" to listOf("🧺 生活"),
        "reading" to listOf("🧺 生活", "📚 ことば・学び"),
        "discomfort" to listOf("📡 実データ"),
        "cooking" to listOf("🧺 生活"),
        "cleaning" to listOf("🧺 生活"),
        "janken" to listOf("🎲 お遊び"),
        "dice" to listOf("🎲 お遊び"),
        "roulette" to listOf("🎲 お遊び"),
        "meigen" to listOf("📚 ことば・学び", "🎲 お遊び"),
        "age-planet" to listOf("🎲 お遊び", "🕐 時計と暦"),
        "cat-word" to listOf("🎲 お遊び", "📚 ことば・学び"),
        "month-only" to listOf("🕐 時計と暦"),
        "day-left" to listOf("🕐 時計と暦"),
        "color-today" to listOf("🎨 アート", "📚 ことば・学び"),
        "greeting" to listOf("📚 ことば・学び", "🎲 お遊び"),
        "kaleido" to listOf("🎨 アート"),
        "rain-window" to listOf("🎨 アート"),
        "topo" to listOf("🎨 アート"),
        "bubbles" to listOf("🎨 アート"),
        "tile-pattern" to listOf("🎨 アート"),
        "meds" to listOf("🧺 生活"),
        "plant" to listOf("🧺 生活"),
        "fridge" to listOf("🧺 生活"),
        "sleep-debt" to listOf("🧺 生活"),
        "eye-rest" to listOf("🧺 生活"),
        "fortune-cookie" to listOf("🎲 お遊び"),
        "lucky" to listOf("🎲 お遊び"),
        "monster-egg" to listOf("🎲 お遊び"),
        "tide" to listOf("📡 実データ"),
        "quake-count" to listOf("📡 実データ"),
        "era" to listOf("🕐 時計と暦"),
        "seconds-today" to listOf("🕐 時計と暦"),
        "proverb" to listOf("📚 ことば・学び"),
        "math-drill" to listOf("📚 ことば・学び"),
        "pref" to listOf("📚 ことば・学び"),
        "constellation" to listOf("📚 ことば・学び"),
        "coffee" to listOf("🧺 生活"),
        "dream" to listOf("🧺 生活"),
        "gratitude" to listOf("🧺 生活"),
        "chord" to listOf("📚 ことば・学び", "🎲 お遊び"),
    )

    /** Tag display order in the feed's filter row. */
    val tagOrder: List<String> = listOf(
        "📡 実データ",
        "🎨 アート",
        "🧺 生活",
        "🎲 お遊び",
        "🕐 時計と暦",
        "📚 ことば・学び",
    )

    val all: List<Pair<String, String>> = listOf(
        "🌤 東京の天気" to """
// プリセット: 東京の天気 3.2（Open-Meteo、APIキー不要の実データ）
// 3.1の全機能（気温曲線・降水確率・太陽の弧・7日間予報・時間帯パレット）に
// 「キャッシュ優先」を追加：30分以内のデータが端末内にあれば通信せず即表示し、
// 時間切れそのものを起きにくくした。古い時だけ取りに行き、失敗したら前回分を出す。
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
function codeKanji(c) {
  if (c === 0) return "快晴";
  if (c <= 2) return "晴れ";
  if (c === 3) return "曇り";
  if (c === 45 || c === 48) return "霧";
  if (c >= 51 && c <= 57) return "霧雨";
  if (c >= 61 && c <= 67) return "雨";
  if (c >= 71 && c <= 77) return "雪";
  if (c >= 80 && c <= 82) return "にわか雨";
  if (c >= 95) return "雷雨";
  return "—";
}
const pad2 = function (n) { return String(n).padStart(2, "0"); };
const DIRS = ["北", "北北東", "北東", "東北東", "東", "東南東", "南東", "南南東", "南", "南南西", "南西", "西南西", "西", "西北西", "北西", "北北西"];
function uvLabel(uv) {
  if (uv == null) return "—";
  if (uv < 3) return "弱い";
  if (uv < 6) return "中程度";
  if (uv < 8) return "強い";
  if (uv < 11) return "非常に強い";
  return "極端に強い";
}
function tempColor(t) {
  if (t < 5) return "#7cc7f0";
  if (t < 13) return "#4dd0e1";
  if (t < 20) return "#aed581";
  if (t < 27) return "#ffb74d";
  return "#ff8a65";
}

async function loadWeather() {
  const url = "https://api.open-meteo.com/v1/forecast?latitude=35.6762&longitude=139.6503" +
    "&current=temperature_2m,apparent_temperature,weather_code,relative_humidity_2m,wind_speed_10m,wind_direction_10m,pressure_msl" +
    "&hourly=temperature_2m,precipitation_probability" +
    "&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_probability_max,uv_index_max" +
    "&forecast_days=7&timezone=Asia%2FTokyo";
  // Cache-first: data younger than 30 minutes renders instantly with NO
  // network at all, so a slow connection cannot time the widget out.
  let cached = null;
  const raw = Storage.get("wx_cache");
  if (raw) {
    try {
      const d = JSON.parse(raw);
      // Caches written by older versions lack the new fields; treat as a miss.
      if (d.hourly && d.hourly.precipitation_probability && d.daily && d.daily.uv_index_max) {
        cached = { d: d, cachedAt: Number(Storage.get("wx_cache_at") || 0), stale: false };
      }
    } catch (e) { /* broken cache: ignore and fetch */ }
  }
  if (cached && Date.now() - cached.cachedAt < 30 * 60 * 1000) return cached;
  try {
    const res = await fetch(url);
    const d = await res.json();
    Storage.set("wx_cache", JSON.stringify(d));
    Storage.set("wx_cache_at", String(Date.now()));
    return { d: d, cachedAt: 0, stale: false };
  } catch (e) {
    if (cached) { cached.stale = true; return cached; }
    throw e;
  }
}

const now = new Date();
const hour = now.getHours();
// Soft palettes by time of day: night is a gentle indigo (a beautiful sky,
// not a rugged navy); morning/day/dusk are light pastels with warm ink.
const PALETTES = [
  { bg: "#34365E", ink: "#FFFFFF", sub: "#C9CDF2", accent: "#FFC7DD", curve: "#AEE7FF", precip: "#8FD0F5", track: "#ffffff2e", todayHi: "#ffffff22", warn: "#FFAB7A" },
  { bg: "#FFE9D2", ink: "#5B4232", sub: "#9A7B5F", accent: "#E65100", curve: "#E8873A", precip: "#3FA9D9", track: "#5b42331f", todayHi: "#ffffff80", warn: "#E65100" },
  { bg: "#DFF4FC", ink: "#23485C", sub: "#5E8296", accent: "#0277BD", curve: "#2E97C9", precip: "#3FA9D9", track: "#23485c1f", todayHi: "#ffffff80", warn: "#E65100" },
  { bg: "#F9DCE9", ink: "#55263F", sub: "#96617C", accent: "#C2185B", curve: "#D6337C", precip: "#7A6FD0", track: "#55263f1f", todayHi: "#ffffff80", warn: "#C2185B" },
];
const pal = PALETTES[hour < 5 || hour >= 19 ? 0 : hour < 10 ? 1 : hour < 16 ? 2 : 3];
const NOW_LINE = "#F08A3C";

const widget = new ListWidget();
widget.backgroundColor = new Color(pal.bg);
widget.setPadding(14, 14, 14, 14);

const head = widget.addStack();
head.layoutHorizontally();
head.alignItems = "center";
const title = head.addText("🌤 東京の天気");
title.font = Font.boldSystemFont(14);
title.textColor = new Color(pal.accent);
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
  upd.textColor = new Color(loaded.cachedAt > 0 ? pal.warn : pal.sub);

  // ---- Hero: condition + big temperature ----
  widget.addSpacer(6);
  const info = codeInfo(d.current.weather_code);
  const big = widget.addStack();
  big.layoutHorizontally();
  big.alignItems = "center";
  const em = big.addText(info[0]);
  em.font = Font.systemFont(30);
  const temp = big.addText(" " + Math.round(d.current.temperature_2m) + "°");
  temp.font = Font.boldSystemFont(34);
  temp.textColor = new Color(pal.ink);
  const lab = big.addText(" " + info[1]);
  lab.font = Font.systemFont(14);
  lab.textColor = new Color(pal.sub);
  const feel = big.addText(
    "  体感 " + Math.round(d.current.apparent_temperature) + "° ／ " +
    Math.round(d.daily.temperature_2m_max[0]) + "°-" + Math.round(d.daily.temperature_2m_min[0]) + "°"
  );
  feel.font = Font.systemFont(11);
  feel.textColor = new Color(pal.sub);

  // ---- Canvas 1: 24h temperature curve + precipitation probability ----
  widget.addSpacer(8);
  const temps = d.hourly.temperature_2m.slice(0, 24);
  const probs = (d.hourly.precipitation_probability || []).slice(0, 24);
  let tMin = Math.min.apply(null, temps);
  let tMax = Math.max.apply(null, temps);
  if (tMax - tMin < 4) { tMax += 2; tMin -= 2; }
  const W = 304, plotL = 8, plotR = 296, plotT = 12, plotB = 46, barBase = 62;
  const cv = widget.addCanvas(W, 76);
  const xFor = function (h) { return plotL + (plotR - plotL) * (h / 23); };
  const yFor = function (t) { return plotB - (plotB - plotT) * ((t - tMin) / (tMax - tMin)); };
  for (let h = 0; h < 24; h++) {
    const p = probs[h] || 0;
    if (p > 0) cv.rect(xFor(h) - 3, barBase - Math.round(p * 0.14), 6, Math.round(p * 0.14), pal.precip);
    if (h > 0) cv.line(xFor(h - 1), yFor(temps[h - 1]), xFor(h), yFor(temps[h]), pal.curve, 2);
  }
  for (let h = 0; h < 24; h += 3) cv.circle(xFor(h), yFor(temps[h]), 2, pal.ink);
  const nowX = xFor(hour + now.getMinutes() / 60);
  cv.line(nowX, 8, nowX, barBase, NOW_LINE, 2);
  cv.circle(nowX, yFor(d.current.temperature_2m), 3, NOW_LINE);
  cv.text(plotL, 0, Math.round(tMax) + "°", 9, pal.ink, false);
  cv.text(plotL, plotB - 2, Math.round(tMin) + "°", 9, pal.sub, false);
  const pMax = Math.max.apply(null, probs.concat([0]));
  if (pMax > 0) cv.text(plotR - 52, barBase - 16, "降水 " + pMax + "%", 9, pal.precip, false);
  const hourMarks = [0, 6, 12, 18, 23];
  for (const hm of hourMarks) cv.text(xFor(hm) - 6, 66, hm + "時", 8, pal.sub, false);

  // ---- Canvas 2: the sun's arc ----
  const riseMin = Number(String(d.daily.sunrise[0]).slice(11, 13)) * 60 + Number(String(d.daily.sunrise[0]).slice(14, 16));
  const setMin = Number(String(d.daily.sunset[0]).slice(11, 13)) * 60 + Number(String(d.daily.sunset[0]).slice(14, 16));
  const nowMin = hour * 60 + now.getMinutes();
  const sunT = Math.max(0, Math.min(1, (nowMin - riseMin) / Math.max(1, setMin - riseMin)));
  const sc = widget.addCanvas(W, 40);
  const cx = 152, horizon = 32, rx = 128, ry = 24;
  sc.line(8, horizon, 296, horizon, pal.sub, 1);
  let px = cx - rx, py = horizon;
  for (let i = 1; i <= 24; i++) {
    const a = Math.PI * (i / 24);
    const x = cx - rx * Math.cos(a);
    const y = horizon - ry * Math.sin(a);
    sc.line(px, py, x, y, "#F5A623", 2);
    px = x; py = y;
  }
  const sunA = Math.PI * sunT;
  const sunX = cx - rx * Math.cos(sunA);
  const sunY = horizon - ry * Math.sin(sunA);
  sc.circle(sunX, sunY, nowMin >= riseMin && nowMin <= setMin ? 5 : 3, "#FFB300");
  const dayLen = setMin - riseMin;
  const sunLine = widget.addText(
    "🌅 " + String(d.daily.sunrise[0]).slice(11, 16) + "　🌇 " + String(d.daily.sunset[0]).slice(11, 16) +
    "　☀️ 日照 " + Math.floor(dayLen / 60) + "時間" + pad2(dayLen % 60) + "分"
  );
  sunLine.font = Font.systemFont(11);
  sunLine.textColor = new Color(pal.sub);

  // ---- Detail line: humidity / wind / pressure / UV ----
  const dir = DIRS[Math.round((d.current.wind_direction_10m || 0) / 22.5) % 16];
  const det = widget.addText(
    "💧湿度 " + d.current.relative_humidity_2m + "%　🌬️風 " + dir + " " + Math.round(d.current.wind_speed_10m) + "km/h" +
    "　🧭気圧 " + Math.round(d.current.pressure_msl) + "hPa　☀️UV " + uvLabel(d.daily.uv_index_max[0])
  );
  det.font = Font.systemFont(11);
  det.textColor = new Color(pal.sub);

  // ---- Canvas 3: 7-day forecast with temperature range bars ----
  widget.addSpacer(8);
  let wMin = Infinity, wMax = -Infinity;
  for (let i = 0; i < 7; i++) {
    wMin = Math.min(wMin, d.daily.temperature_2m_min[i]);
    wMax = Math.max(wMax, d.daily.temperature_2m_max[i]);
  }
  if (wMax - wMin < 6) { wMax += 3; wMin -= 3; }
  const fc = widget.addCanvas(W, 130);
  const WD = ["日", "月", "火", "水", "木", "金", "土"];
  const barL = 128, barR = 262;
  const bx = function (t) { return barL + (barR - barL) * ((t - wMin) / (wMax - wMin)); };
  for (let i = 0; i < 7; i++) {
    const date = new Date(now.getFullYear(), now.getMonth(), now.getDate() + i);
    const y = 4 + i * 18;
    if (i === 0) {
      fc.roundRect(0, y - 2, W, 17, 6, pal.todayHi);
      fc.text(4, y + 1, "今日", 10, pal.warn, true);
    } else {
      fc.text(4, y + 1, WD[date.getDay()] + "曜", 10, pal.ink, false);
    }
    fc.text(36, y + 1, codeKanji(d.daily.weather_code[i]), 10, pal.ink, false);
    const rain = d.daily.precipitation_probability_max[i] || 0;
    if (rain >= 30) fc.text(74, y + 1, rain + "%", 9, pal.precip, false);
    fc.text(100, y + 1, Math.round(d.daily.temperature_2m_min[i]) + "°", 10, pal.sub, false);
    fc.roundRect(barL, y + 3, barR - barL, 7, 3, pal.track);
    const x1 = bx(d.daily.temperature_2m_min[i]);
    const x2 = bx(d.daily.temperature_2m_max[i]);
    const mid = (x1 + x2) / 2;
    fc.roundRect(x1, y + 3, Math.max(3, mid - x1), 7, 3, tempColor(d.daily.temperature_2m_min[i]));
    fc.roundRect(mid, y + 3, Math.max(3, x2 - mid), 7, 3, tempColor(d.daily.temperature_2m_max[i]));
    if (i === 0) {
      const cxNow = bx(d.current.temperature_2m);
      fc.circle(cxNow, y + 6, 3, pal.ink);
    }
    fc.text(270, y + 1, Math.round(d.daily.temperature_2m_max[i]) + "°", 10, pal.ink, true);
  }

  if (loaded.stale) {
    const note = widget.addText("※ 通信できないため、前回取得したデータを表示しています");
    note.font = Font.systemFont(10);
    note.textColor = new Color(pal.warn);
  }
} catch (e) {
  const err = widget.addText("取得に失敗: " + e.message);
  err.font = Font.systemFont(13);
  err.textColor = new Color(pal.warn);
  const hint = widget.addText("通信できる場所で、ウィジェットをタップすると再取得します。");
  hint.font = Font.systemFont(11);
  hint.textColor = new Color(pal.sub);
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
// プリセット: 年末カウントダウン 3.1（アプリと同じ「美しく・かわいい」世界）
// クリーム色の地に、パステルの三重リング（年・月・今日の経過）、残り時間の
// 精密な内訳、12月カレンダー、月目盛りつき年プログレスバーをCanvasで描く。
const widget = new ListWidget();
widget.backgroundColor = new Color("#FFF6EC");
widget.setPadding(14, 14, 14, 14);

const INK = "#4A3428";
const SUB = "#9A7B5F";
const FAINT = "#C9AF97";
const TRACK = "#F3E3CE";
const PINK = "#F78FB8";
const ORANGE = "#FFAB7A";
const SKY = "#7CC7F0";
const HOT = "#E65100";

const now = new Date();
const y = now.getFullYear();
const startOfDay = new Date(y, now.getMonth(), now.getDate());
const yearStart = new Date(y, 0, 1);
const nextYear = new Date(y + 1, 0, 1);
const dayOfYear = Math.floor((startOfDay - yearStart) / 86400000) + 1;
const daysInYear = Math.floor((nextYear - yearStart) / 86400000);
const left = daysInYear - dayOfYear;
const yearFrac = (now - yearStart) / (nextYear - yearStart);

const monthStart = new Date(y, now.getMonth(), 1);
const nextMonth = new Date(y, now.getMonth() + 1, 1);
const monthFrac = (now - monthStart) / (nextMonth - monthStart);
const dayFrac = (now - startOfDay) / 86400000;

const msLeft = nextYear - now;
const hoursLeft = Math.floor(msLeft / 3600000);
const minsLeft = Math.floor((msLeft % 3600000) / 60000);
const weeks = Math.floor(left / 7);
const extraDays = left % 7;

// Remaining weekend days (Sat/Sun), today included.
let weekendLeft = 0;
for (let i = 0; i <= left; i++) {
  const dow = new Date(y, now.getMonth(), now.getDate() + i).getDay();
  if (dow === 0 || dow === 6) weekendLeft++;
}

const row = widget.addStack();
row.layoutHorizontally();

// ---- Triple rings: year / month / day progress (pastel) ----
const cv = row.addCanvas(124, 124);
cv.ring(62, 62, 53, 9, -90, 360, TRACK);
cv.ring(62, 62, 53, 9, -90, 360 * yearFrac, PINK);
cv.ring(62, 62, 41, 8, -90, 360, TRACK);
cv.ring(62, 62, 41, 8, -90, 360 * monthFrac, ORANGE);
cv.ring(62, 62, 30, 7, -90, 360, TRACK);
cv.ring(62, 62, 30, 7, -90, 360 * dayFrac, SKY);
const pct = (yearFrac * 100).toFixed(1) + "%";
cv.text(62 - (pct.length * 5.4), 48, pct, 16, INK, true);
cv.text(45, 68, "今年の経過", 9, SUB, false);

const side = row.addStack();
side.setPadding(8, 14, 0, 0);
if (left === 0) {
  const t0 = side.addText("今日は大晦日！");
  t0.font = Font.boldSystemFont(20);
  t0.textColor = new Color("#DE5D8A");
  const t0b = side.addText(y + "年、最後の1日です");
  t0b.font = Font.systemFont(12);
  t0b.textColor = new Color(INK);
} else {
  const t1 = side.addText(y + "年もあと");
  t1.font = Font.systemFont(13);
  t1.textColor = new Color(SUB);
  const t2 = side.addText(left + "日");
  t2.font = Font.boldSystemFont(38);
  t2.textColor = new Color(INK);
  const t3 = side.addText("＝ " + weeks + "週間と" + extraDays + "日");
  t3.font = Font.systemFont(12);
  t3.textColor = new Color("#DE5D8A");
}
const t4 = side.addText("大晦日まで " + hoursLeft + "時間" + minsLeft + "分");
t4.font = Font.systemFont(12);
t4.textColor = new Color(INK);
const t5 = side.addText("今日は " + dayOfYear + " 日目 ／ 残りの土日 " + weekendLeft + "日");
t5.font = Font.systemFont(12);
t5.textColor = new Color(SUB);
const legend = side.addText("◉ 年 · 月 · 今日 の経過");
legend.font = Font.systemFont(10);
legend.textColor = new Color(FAINT);

// ---- December calendar (Canvas): past days faint, today ringed, 31st pink ----
widget.addSpacer(10);
const dec1 = new Date(y, 11, 1);
const firstDow = dec1.getDay();
const calW = 304, cellW = 40, cellH = 19, gridX = 12, gridY = 16;
const rows = Math.ceil((firstDow + 31) / 7);
const cal = widget.addCanvas(calW, gridY + rows * cellH + 4);
cal.text(2, 0, "12月", 11, INK, true);
const WD = ["日", "月", "火", "水", "木", "金", "土"];
for (let c = 0; c < 7; c++) {
  cal.text(gridX + c * cellW + 8, 2, WD[c], 9, c === 0 ? "#DE5D8A" : c === 6 ? "#4FA3D1" : FAINT, false);
}
const inDecember = now.getMonth() === 11;
for (let day = 1; day <= 31; day++) {
  const idx = firstDow + day - 1;
  const cxp = gridX + (idx % 7) * cellW + 13;
  const cyp = gridY + Math.floor(idx / 7) * cellH + 8;
  const isPast = inDecember && day < now.getDate();
  const isToday = inDecember && day === now.getDate();
  if (day === 31) {
    cal.circle(cxp, cyp, 8, PINK);
    cal.text(cxp - 6, cyp - 5, "31", 9, "#FFFFFF", true);
  } else if (isToday) {
    cal.ring(cxp, cyp, 8, 2, 0, 360, HOT);
    cal.text(cxp - (day < 10 ? 3 : 6), cyp - 5, String(day), 9, HOT, true);
  } else {
    cal.text(
      cxp - (day < 10 ? 3 : 6), cyp - 5, String(day), 9,
      isPast ? "#DEC9B0" : "#6B4F3A", false
    );
  }
}

// ---- Year progress bar with month ticks ----
widget.addSpacer(6);
const bar = widget.addCanvas(304, 26);
bar.roundRect(0, 4, 304, 8, 4, TRACK);
bar.roundRect(0, 4, Math.max(6, 304 * yearFrac), 8, 4, PINK);
for (let m = 1; m < 12; m++) {
  const tx = 304 * ((new Date(y, m, 1) - yearStart) / (nextYear - yearStart));
  bar.line(tx, 4, tx, 12, "#FFF6EC", 1);
  bar.text(tx - 3, 15, String(m + 1), 8, FAINT, false);
}
bar.text(0, 15, "1", 8, FAINT, false);

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
        "🎞 ゆらゆらボール（アニメ実験）" to """
// プリセット: ゆらゆらボール（アニメ実験）
// 「Scriptlet アニメ（実験）」ウィジェット専用。Script.animate でアニメ宣言し、
// ホストが frame（コマ番号）と elapsedMs（開始からの経過）を渡して毎コマ再実行する。
// 通常ウィジェットに置くと frame 0 の静止画になります。
Script.animate({ fps: 5 });

const widget = new ListWidget();
const cv = widget.addCanvas(220, 130);

const t = elapsedMs / 1000;

// 紙（角丸・クリーム）
cv.roundRect(0, 0, 220, 130, 24, "#FFF6EC");

// 右上の掃引リング（オレンジ）
const sweep = (t * 120) % 360;
cv.ring(188, 30, 13, 5, -90, 360, "#FFEDD9");
cv.ring(188, 30, 13, 5, -90, sweep, "#E65100");

// 大きいボール（ピンク）: 左右に揺れながら跳ねる
const bx = 104 + Math.sin(t * 1.4) * 66;
const bounce = Math.abs(Math.sin(t * 2.6));
const by = 98 - bounce * 42;
cv.circle(bx, 113, 15 - bounce * 5, "#F3D9E2"); // 影
cv.circle(bx, by, 17, "#F78FB8");
cv.circle(bx - 6, by - 6, 6, "#FCE7F1"); // ハイライト

// 小さいボール（紫）: 位相をずらして後を追う
const sx = 104 + Math.sin(t * 1.4 - 1.2) * 66;
const sbounce = Math.abs(Math.sin(t * 2.6 - 0.9));
const sy = 102 - sbounce * 30;
cv.circle(sx, sy, 9, "#CE9BE8");
cv.circle(sx - 3, sy - 3, 3, "#F1E6F7");

cv.text(12, 110, "frame " + frame, 11, "#6B5560", false);

Script.setWidget(widget);
""".trimIndent(),
        "🌀 日替わりマンダラ" to """
// プリセット: 日替わりマンダラ（日付シードの万華鏡アート、ネット不要）
// 対称に線と円を重ねるだけなのに、思いがけない模様が生まれる
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
const rnd = mulberry32(seedNum * 7 + 3);
const palette = ["#f78fb8", "#7cc7f0", "#ce9be8", "#ffab7a", "#facc15"];

const widget = new ListWidget();
widget.backgroundColor = new Color("#171126");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🌀 日替わりマンダラ");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);

const S = 232, C = S / 2;
const cv = widget.addCanvas(S, S);
cv.rect(0, 0, S, S, "#171126");
const petals = 6 + Math.floor(rnd() * 6); // 6〜11枚対称
const layers = 3 + Math.floor(rnd() * 2);
for (let l = 0; l < layers; l++) {
  const rBase = 26 + l * 30 + rnd() * 10;
  const col = palette[Math.floor(rnd() * palette.length)];
  const dots = rnd() < 0.5;
  for (let i = 0; i < petals; i++) {
    const a = (Math.PI * 2 * i) / petals + l * 0.18;
    const x = C + Math.cos(a) * rBase, y = C + Math.sin(a) * rBase;
    if (dots) cv.circle(x, y, 5 + rnd() * 5, col);
    else cv.ring(x, y, 7 + rnd() * 6, 2.2, 0, 360, col);
    const x2 = C + Math.cos(a + 0.31) * (rBase + 13), y2 = C + Math.sin(a + 0.31) * (rBase + 13);
    cv.line(x, y, x2, y2, col, 1.4);
  }
  cv.ring(C, C, rBase - 12, 1.2, 0, 360, col);
}
cv.circle(C, C, 9, palette[Math.floor(rnd() * palette.length)]);
widget.addSpacer(6);
const foot = widget.addText((now.getMonth() + 1) + "/" + now.getDate() + " の模様（毎日変わります）");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#9d8fc7");
Script.setWidget(widget);
""".trimIndent(),
        "👾 日替わりドット絵" to """
// プリセット: 日替わりドット絵（左右対称モンスター生成、ネット不要）
// 12x12の左半分を乱数で決めて鏡写しにする、懐かしの生成テクニック
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
const rnd = mulberry32(seedNum * 13 + 5);
const bodies = ["#f78fb8", "#7cc7f0", "#ce9be8", "#ffab7a", "#86efac", "#facc15"];
const body = bodies[Math.floor(rnd() * bodies.length)];

const widget = new ListWidget();
widget.backgroundColor = new Color("#101828");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("👾 日替わりドット絵");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);

const N = 12, CELL = 15, OFF = (232 - N * CELL) / 2;
const cv = widget.addCanvas(232, 190);
cv.rect(0, 0, 232, 190, "#0b1220");
const grid = [];
for (let y = 0; y < N; y++) {
  grid.push([]);
  for (let x = 0; x < N / 2; x++) grid[y].push(rnd() < 0.42);
}
for (let y = 0; y < N; y++) {
  for (let x = 0; x < N; x++) {
    const on = x < N / 2 ? grid[y][x] : grid[y][N - 1 - x];
    if (on) cv.rect(OFF + x * CELL + 1, 5 + y * CELL + 1, CELL - 2, CELL - 2, body);
  }
}
// 目だけ白黒で入れると一気に「生き物」になる
const ey = 3 + Math.floor(rnd() * 3);
cv.rect(OFF + 3 * CELL + 4, 5 + ey * CELL + 4, 6, 6, "#101828");
cv.rect(OFF + 8 * CELL + 4, 5 + ey * CELL + 4, 6, 6, "#101828");
widget.addSpacer(6);
const foot = widget.addText("今日のモンスター（明日は別の子）");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#8ea2c9");
Script.setWidget(widget);
""".trimIndent(),
        "🌊 波のアート" to """
// プリセット: 波のアート（サイン波の重ね描き、ネット不要）
// 位相が時刻で流れるので、更新するたびに少し違う波になる
const now = new Date();
const phase = (now.getHours() * 60 + now.getMinutes()) / 22;

const widget = new ListWidget();
widget.backgroundColor = new Color("#0d1b2e");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🌊 波のアート");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);

const W = 232, H = 150;
const cv = widget.addCanvas(W, H);
cv.rect(0, 0, W, H, "#0d1b2e");
const cols = ["#7cc7f0", "#38bdf8", "#818cf8", "#f78fb8", "#ffab7a"];
for (let w = 0; w < 5; w++) {
  const amp = 12 + w * 7, base = 34 + w * 20, fr = 0.021 + w * 0.006;
  let px = 0, py = base + Math.sin(phase + w) * amp;
  for (let x = 4; x <= W - 4; x += 4) {
    const y = base + Math.sin(x * fr + phase + w * 1.7) * amp;
    cv.line(px, py, x, y, cols[w], 2.2);
    px = x; py = y;
  }
}
widget.addSpacer(6);
const foot = widget.addText("更新するたび、波は流れています");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#7d94b8");
Script.setWidget(widget);
""".trimIndent(),
        "🌳 フラクタルツリー" to """
// プリセット: フラクタルツリー（再帰で枝分かれする木、ネット不要）
// 枝の角度が日替わり。短い再帰で「計算で絵が生まれる」感覚を味わう
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
const rnd = mulberry32(seedNum * 3 + 11);
const spread = 0.32 + rnd() * 0.3;
const lean = (rnd() - 0.5) * 0.14;

const widget = new ListWidget();
widget.backgroundColor = new Color("#12211a");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🌳 フラクタルツリー");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);

const W = 232, H = 185;
const cv = widget.addCanvas(W, H);
cv.rect(0, 0, W, H, "#0d1a13");
cv.line(8, H - 6, W - 8, H - 6, "#2c4a3a", 2);
function branch(x, y, angle, len, depth) {
  if (depth === 0 || len < 3) {
    cv.circle(x, y, 2.4, "#86efac");
    return;
  }
  const nx = x + Math.sin(angle) * len, ny = y - Math.cos(angle) * len;
  cv.line(x, y, nx, ny, depth > 5 ? "#a1805a" : "#c9a06a", depth > 5 ? 3 : 1.6);
  branch(nx, ny, angle - spread + lean, len * 0.74, depth - 1);
  branch(nx, ny, angle + spread + lean, len * 0.74, depth - 1);
}
branch(W / 2, H - 6, 0, 46, 8);
widget.addSpacer(6);
const foot = widget.addText("枝ぶりは日替わり（再帰の深さ 8）");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#7fae8f");
Script.setWidget(widget);
""".trimIndent(),
        "🪐 太陽系のいま" to """
// プリセット: 太陽系のいま（惑星の位置を計算して描く、ネット不要）
// 平均軌道要素からの近似計算。今日の惑星配置がそのまま絵になる
const now = new Date();
const days = (now.getTime() / 86400000) - 10957.5; // J2000.0 からの日数
// [名前, 公転周期(日), 軌道半径の表示スケール, 色, 元期の平均黄経(度)]
const planets = [
  ["水星", 88, 26, "#d6c7b2", 252.3],
  ["金星", 225, 40, "#facc15", 181.9],
  ["地球", 365, 54, "#7cc7f0", 100.4],
  ["火星", 687, 68, "#ff8a5c", 355.4],
  ["木星", 4333, 84, "#e8b98a", 34.4],
  ["土星", 10759, 100, "#e7d8a8", 49.9]
];
const widget = new ListWidget();
widget.backgroundColor = new Color("#0a0f1e");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🪐 太陽系のいま");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);

const S = 232, C = S / 2;
const cv = widget.addCanvas(S, S);
cv.rect(0, 0, S, S, "#0a0f1e");
// 背景の星
for (let i = 0; i < 40; i++) {
  const sx = (i * 53 + 17) % S, sy = (i * 91 + 29) % S;
  cv.circle(sx, sy, i % 5 === 0 ? 1.6 : 1, "#3d4a6b");
}
cv.circle(C, C, 8, "#facc15");
cv.ring(C, C, 11, 1.4, 0, 360, "#b45309");
planets.forEach(function (p) {
  const ang = ((p[4] + 360 * days / p[1]) % 360) * Math.PI / 180;
  cv.ring(C, C, p[2], 0.7, 0, 360, "#26314d");
  const x = C + Math.cos(ang) * p[2], y = C + Math.sin(ang) * p[2];
  cv.circle(x, y, p[0] === "地球" ? 4.4 : 3.4, p[3]);
});
widget.addSpacer(6);
const foot = widget.addText("今日の惑星配置（近似計算・上が北極側）");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#7d8db0");
Script.setWidget(widget);
""".trimIndent(),
        "🧩 日替わり迷路" to """
// プリセット: 日替わり迷路（穴掘り法で生成、ネット不要）
// スタート左上、ゴール右下。目で追って解く小さな迷路
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
const rnd = mulberry32(seedNum * 17 + 23);

const COLS = 11, ROWS = 9, CELL = 19;
const W = COLS * CELL + 2, H = ROWS * CELL + 2;
// walls[y][x] = [top, right, bottom, left]
const walls = [], seen = [];
for (let y = 0; y < ROWS; y++) {
  walls.push([]); seen.push([]);
  for (let x = 0; x < COLS; x++) { walls[y].push([1, 1, 1, 1]); seen[y].push(false); }
}
const stack = [[0, 0]];
seen[0][0] = true;
const dirs = [[0, -1, 0, 2], [1, 0, 1, 3], [0, 1, 2, 0], [-1, 0, 3, 1]];
while (stack.length) {
  const cur = stack[stack.length - 1];
  const cx = cur[0], cy = cur[1];
  const opts = [];
  dirs.forEach(function (d) {
    const nx = cx + d[0], ny = cy + d[1];
    if (nx >= 0 && ny >= 0 && nx < COLS && ny < ROWS && !seen[ny][nx]) opts.push([nx, ny, d[2], d[3]]);
  });
  if (!opts.length) { stack.pop(); continue; }
  const nxny = opts[Math.floor(rnd() * opts.length)];
  walls[cy][cx][nxny[2]] = 0;
  walls[nxny[1]][nxny[0]][nxny[3]] = 0;
  seen[nxny[1]][nxny[0]] = true;
  stack.push([nxny[0], nxny[1]]);
}

const widget = new ListWidget();
widget.backgroundColor = new Color("#191423");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🧩 日替わり迷路");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);
const cv = widget.addCanvas(232, 190);
cv.rect(0, 0, 232, 190, "#120e1c");
const OX = (232 - W) / 2, OY = 6;
cv.circle(OX + CELL / 2 + 1, OY + CELL / 2 + 1, 5, "#86efac");
cv.circle(OX + (COLS - 1) * CELL + CELL / 2 + 1, OY + (ROWS - 1) * CELL + CELL / 2 + 1, 5, "#f78fb8");
for (let y = 0; y < ROWS; y++) {
  for (let x = 0; x < COLS; x++) {
    const x0 = OX + x * CELL + 1, y0 = OY + y * CELL + 1, w = walls[y][x];
    if (w[0]) cv.line(x0, y0, x0 + CELL, y0, "#8b7fb8", 2);
    if (w[1]) cv.line(x0 + CELL, y0, x0 + CELL, y0 + CELL, "#8b7fb8", 2);
    if (w[2]) cv.line(x0, y0 + CELL, x0 + CELL, y0 + CELL, "#8b7fb8", 2);
    if (w[3]) cv.line(x0, y0, x0, y0 + CELL, "#8b7fb8", 2);
  }
}
widget.addSpacer(6);
const foot = widget.addText("緑からピンクへ。明日は新しい迷路");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#9d8fc7");
Script.setWidget(widget);
""".trimIndent(),
        "🌌 今夜の星空" to """
// プリセット: 今夜の星空（日替わりの星屑と星座もどき、ネット不要）
// 星を散らして近い星同士を線で結ぶと、それっぽい星座が生まれる
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
const rnd = mulberry32(seedNum * 19 + 31);

const widget = new ListWidget();
widget.backgroundColor = new Color("#0a1024");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🌌 今夜の星空");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);

const W = 232, H = 168;
const cv = widget.addCanvas(W, H);
cv.rect(0, 0, W, H, "#070d1f");
for (let i = 0; i < 90; i++) {
  cv.circle(rnd() * W, rnd() * H, rnd() < 0.12 ? 1.8 : 1, "#5b6f9e");
}
// 星座もどき：明るい星を6つ置いて順に結ぶ
const stars = [];
for (let i = 0; i < 6; i++) stars.push([24 + rnd() * (W - 48), 22 + rnd() * (H - 44)]);
for (let i = 0; i < stars.length - 1; i++) {
  cv.line(stars[i][0], stars[i][1], stars[i + 1][0], stars[i + 1][1], "#7cc7f0", 1.2);
}
stars.forEach(function (s) {
  cv.circle(s[0], s[1], 3, "#ffffff");
  cv.ring(s[0], s[1], 5.5, 1, 0, 360, "#7cc7f0");
});
// 月
cv.circle(W - 30, 26, 10, "#fde68a");
cv.circle(W - 26, 23, 8.5, "#070d1f");
widget.addSpacer(6);
const foot = widget.addText((now.getMonth() + 1) + "/" + now.getDate() + " の空（想像の星座つき）");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#7d8db0");
Script.setWidget(widget);
""".trimIndent(),
        "📜 今日は何の日" to """
// プリセット: 今日は何の日（Wikipediaの「できごと」から1件、本物のデータ）
// その日に起きた歴史の出来事を1つ。日付が変われば中身も変わる
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
const rnd = mulberry32(seedNum * 53 + 3);
const widget = new ListWidget();
widget.backgroundColor = new Color("#1d1a12");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("📜 今日は何の日");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#facc15");
widget.addSpacer(8);
const dateLine = widget.addText((now.getMonth() + 1) + "月" + now.getDate() + "日");
dateLine.font = Font.boldSystemFont(20);
dateLine.textColor = new Color("#ffffff");
widget.addSpacer(6);
try {
  const pageTitle = encodeURIComponent((now.getMonth() + 1) + "月" + now.getDate() + "日");
  const url = "https://ja.wikipedia.org/w/api.php?action=query&prop=extracts&explaintext=1&format=json&titles=" + pageTitle;
  const res = await fetch(url);
  const d = await res.json();
  const pages = d.query.pages;
  const key = Object.keys(pages)[0];
  const text = pages[key].extract || "";
  const start = text.indexOf("できごと");
  if (start < 0) throw new Error("no section");
  const sectionLines = [];
  let pastHeader = false;
  text.slice(start).split("\n").forEach(function (l) {
    if (/==\s*$/.test(l)) { if (pastHeader) pastHeader = "done"; else pastHeader = true; return; }
    if (pastHeader === true) sectionLines.push(l);
  });
  const lines = sectionLines.filter(function (l) { return /^\d{3,4}年/.test(l); });
  if (!lines.length) throw new Error("empty");
  const line = lines[Math.floor(rnd() * lines.length)];
  const m = line.match(/^(\d{3,4}年)\s*-\s*(.*)$/);
  const yr = widget.addText(m ? m[1] : "");
  yr.font = Font.boldSystemFont(15);
  yr.textColor = new Color("#facc15");
  widget.addSpacer(2);
  const body = m ? m[2] : line;
  const tx = widget.addText(body.length > 90 ? body.slice(0, 90) + "…" : body);
  tx.font = Font.systemFont(13);
  tx.textColor = new Color("#e8e2d0");
  widget.addSpacer(4);
  const src = widget.addText("出典: Wikipedia「" + (now.getMonth() + 1) + "月" + now.getDate() + "日」");
  src.font = Font.systemFont(10);
  src.textColor = new Color("#8a8272");
} catch (e) {
  const err = widget.addText("歴史データを取得できませんでした（通信環境をご確認ください）");
  err.font = Font.systemFont(12);
  err.textColor = new Color("#ffab7a");
}
Script.setWidget(widget);
""".trimIndent(),
        "☂️ 傘いる？" to """
// プリセット: 傘いる？（東京の降水確率だけを超大きく、本物のデータ）
// 天気ぜんぶは要らない。「傘を持つか」だけ知りたい朝のための単機能版
const widget = new ListWidget();
widget.backgroundColor = new Color("#0f2233");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("☂️ 傘いる？（東京）");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#7cc7f0");
widget.addSpacer(10);
try {
  const url = "https://api.open-meteo.com/v1/forecast?latitude=35.6762&longitude=139.6503&daily=precipitation_probability_max&timezone=Asia%2FTokyo";
  const res = await fetch(url);
  const d = await res.json();
  const p = d.daily.precipitation_probability_max[0];
  const big = widget.addText(p == null ? "—" : p + "%");
  big.font = Font.boldSystemFont(44);
  big.textColor = new Color("#ffffff");
  widget.addSpacer(4);
  const verdict = p == null ? "降水確率が分かりません"
    : p >= 50 ? "傘、持っていきましょう"
    : p >= 30 ? "折りたたみがあると安心"
    : "傘はいらなそうです";
  const v = widget.addText(verdict);
  v.font = Font.boldSystemFont(16);
  v.textColor = new Color(p != null && p >= 50 ? "#ffab7a" : "#86efac");
  widget.addSpacer(4);
  const sub = widget.addText("今日の降水確率（最大）");
  sub.font = Font.systemFont(11);
  sub.textColor = new Color("#7d94b8");
} catch (e) {
  const err = widget.addText("天気を取得できませんでした");
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}
Script.setWidget(widget);
""".trimIndent(),
        "👕 洗濯指数" to """
// プリセット: 洗濯指数（外干しできる？を天気から判定、本物のデータ）
// 降水確率・気温・風から「干せる／微妙／部屋干し」を出す生活指数
const widget = new ListWidget();
widget.backgroundColor = new Color("#10222b");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("👕 洗濯指数（東京）");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#7cc7f0");
widget.addSpacer(10);
try {
  const url = "https://api.open-meteo.com/v1/forecast?latitude=35.6762&longitude=139.6503&daily=precipitation_probability_max,temperature_2m_max,wind_speed_10m_max&timezone=Asia%2FTokyo";
  const res = await fetch(url);
  const d = await res.json();
  const rain = d.daily.precipitation_probability_max[0] || 0;
  const temp = d.daily.temperature_2m_max[0];
  const wind = d.daily.wind_speed_10m_max[0];
  let score = 100 - rain;
  if (temp < 10) score -= 15;
  if (wind >= 20) score -= 10;
  if (wind < 5) score -= 5;
  score = Math.max(0, Math.min(100, Math.round(score)));
  const big = widget.addText(score + "");
  big.font = Font.boldSystemFont(44);
  big.textColor = new Color("#ffffff");
  widget.addSpacer(2);
  const verdict = score >= 70 ? "外干し日和です" : score >= 40 ? "干せるけど様子見で" : "今日は部屋干しが無難";
  const v = widget.addText(verdict);
  v.font = Font.boldSystemFont(16);
  v.textColor = new Color(score >= 70 ? "#86efac" : score >= 40 ? "#facc15" : "#ffab7a");
  widget.addSpacer(4);
  const sub = widget.addText("降水 " + rain + "%・最高 " + Math.round(temp) + "℃・風 " + Math.round(wind) + "km/h");
  sub.font = Font.systemFont(11);
  sub.textColor = new Color("#7d94b8");
} catch (e) {
  const err = widget.addText("天気を取得できませんでした");
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}
Script.setWidget(widget);
""".trimIndent(),
        "🧴 UV指数" to """
// プリセット: UV指数（今日の紫外線の強さ、本物のデータ）
// 数値と対策をひとこと。日焼け止めを塗るかどうかの判断材料に
const widget = new ListWidget();
widget.backgroundColor = new Color("#2b1a10");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🧴 UV指数（東京）");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffab7a");
widget.addSpacer(10);
try {
  const url = "https://api.open-meteo.com/v1/forecast?latitude=35.6762&longitude=139.6503&daily=uv_index_max&timezone=Asia%2FTokyo";
  const res = await fetch(url);
  const d = await res.json();
  const uv = d.daily.uv_index_max[0];
  const big = widget.addText(uv == null ? "—" : uv.toFixed(1));
  big.font = Font.boldSystemFont(44);
  big.textColor = new Color("#ffffff");
  widget.addSpacer(4);
  let level, advice, col;
  if (uv == null) { level = "不明"; advice = "データが取れませんでした"; col = "#9ca3af"; }
  else if (uv < 3) { level = "弱い"; advice = "対策は最小限でOK"; col = "#86efac"; }
  else if (uv < 6) { level = "中くらい"; advice = "日焼け止めを塗ると安心"; col = "#facc15"; }
  else if (uv < 8) { level = "強い"; advice = "日焼け止め＋帽子がおすすめ"; col = "#ffab7a"; }
  else { level = "非常に強い"; advice = "昼間の外出は最小限に"; col = "#f87171"; }
  const v = widget.addText(level);
  v.font = Font.boldSystemFont(16);
  v.textColor = new Color(col);
  widget.addSpacer(2);
  const a = widget.addText(advice);
  a.font = Font.systemFont(12);
  a.textColor = new Color("#d8cfc2");
} catch (e) {
  const err = widget.addText("UVデータを取得できませんでした");
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}
Script.setWidget(widget);
""".trimIndent(),
        "🌅 日の出・日の入り" to """
// プリセット: 日の出・日の入り（東京、本物のデータ）
// 朝ランや写真の計画に。日の長さも一緒に出す
function hm(iso) { return iso.slice(11, 16); }
function toMin(iso) { return Number(iso.slice(11, 13)) * 60 + Number(iso.slice(14, 16)); }
const widget = new ListWidget();
widget.backgroundColor = new Color("#241a2e");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🌅 日の出・日の入り（東京）");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ce9be8");
widget.addSpacer(10);
try {
  const url = "https://api.open-meteo.com/v1/forecast?latitude=35.6762&longitude=139.6503&daily=sunrise,sunset&timezone=Asia%2FTokyo";
  const res = await fetch(url);
  const d = await res.json();
  const rise = d.daily.sunrise[0], set = d.daily.sunset[0];
  const row = widget.addStack();
  row.layoutHorizontally();
  const c1 = row.addStack();
  const l1 = c1.addText("日の出");
  l1.font = Font.systemFont(12);
  l1.textColor = new Color("#9d8fc7");
  const t1 = c1.addText(hm(rise));
  t1.font = Font.boldSystemFont(30);
  t1.textColor = new Color("#facc15");
  row.addSpacer();
  const c2 = row.addStack();
  const l2 = c2.addText("日の入り");
  l2.font = Font.systemFont(12);
  l2.textColor = new Color("#9d8fc7");
  const t2 = c2.addText(hm(set));
  t2.font = Font.boldSystemFont(30);
  t2.textColor = new Color("#ffab7a");
  widget.addSpacer(8);
  const len = toMin(set) - toMin(rise);
  const sub = widget.addText("日の長さ " + Math.floor(len / 60) + "時間" + (len % 60) + "分");
  sub.font = Font.systemFont(12);
  sub.textColor = new Color("#d8cfc2");
} catch (e) {
  const err = widget.addText("太陽データを取得できませんでした");
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}
Script.setWidget(widget);
""".trimIndent(),
        "💱 為替レート" to """
// プリセット: 為替レート（ドル・ユーロ・ポンドの対円、本物のデータ）
// チャート版（fx-chart）の簡易版。数字だけを大きく、すぐ見たい人向け
const widget = new ListWidget();
widget.backgroundColor = new Color("#0f2417");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("💱 為替レート");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#86efac");
widget.addSpacer(10);
try {
  const res = await fetch("https://api.frankfurter.dev/v1/latest?base=USD&symbols=JPY,EUR,GBP");
  const d = await res.json();
  const jpy = d.rates.JPY, eur = d.rates.EUR, gbp = d.rates.GBP;
  const rows = [
    ["🇺🇸 ドル", jpy],
    ["🇪🇺 ユーロ", jpy / eur],
    ["🇬🇧 ポンド", jpy / gbp]
  ];
  rows.forEach(function (r, i) {
    if (i > 0) widget.addSpacer(6);
    const row = widget.addStack();
    row.layoutHorizontally();
    row.alignItems = "center";
    const name = row.addText(r[0]);
    name.font = Font.systemFont(14);
    name.textColor = new Color("#d8e8d8");
    row.addSpacer();
    const val = row.addText("¥" + r[1].toFixed(1));
    val.font = Font.boldSystemFont(22);
    val.textColor = new Color("#ffffff");
  });
  widget.addSpacer(8);
  const sub = widget.addText("基準日 " + d.date + "（欧州中央銀行の参照レート）");
  sub.font = Font.systemFont(11);
  sub.textColor = new Color("#7fae8f");
} catch (e) {
  const err = widget.addText("レートを取得できませんでした");
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}
Script.setWidget(widget);
""".trimIndent(),
        "🗑 今日のゴミ" to """
// プリセット: ゴミの日（曜日で判定、ネット不要）
// ↓のルールを自分の地域に書き換えて使う想定のテンプレ。
// 0=日 1=月 2=火 3=水 4=木 5=金 6=土 ／「」内は表示名
const RULES = {
  1: "燃えるゴミ", 4: "燃えるゴミ",
  2: "資源ゴミ（缶・びん・ペット）",
  3: "燃えないゴミ",
  5: "プラスチック",
  6: "古紙・ダンボール",
  0: "収集なし"
};
const WD = ["日", "月", "火", "水", "木", "金", "土"];
const now = new Date();
const widget = new ListWidget();
widget.backgroundColor = new Color("#14231d");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🗑 今日のゴミ");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#86efac");
widget.addSpacer(10);
const today = RULES[now.getDay()];
const big = widget.addText(today);
big.font = Font.boldSystemFont(today.length > 8 ? 20 : 26);
big.textColor = new Color("#ffffff");
widget.addSpacer(6);
const tmr = new Date(now.getTime() + 86400000);
const sub = widget.addText("明日（" + WD[tmr.getDay()] + "）は " + RULES[tmr.getDay()]);
sub.font = Font.systemFont(13);
sub.textColor = new Color("#a7c4b5");
widget.addSpacer(4);
const note = widget.addText("※ ルールはコード内の RULES を書き換えてください");
note.font = Font.systemFont(11);
note.textColor = new Color("#5f7a6a");
Script.setWidget(widget);
""".trimIndent(),
        "🎌 次の祝日" to """
// プリセット: 次の祝日（2026〜2027年の日本の祝日入り、ネット不要）
// 「あと何日がんばれば休みか」を数える、小さな希望の表示
const HOLIDAYS = [
  ["2026-01-01", "元日"], ["2026-01-12", "成人の日"], ["2026-02-11", "建国記念の日"],
  ["2026-02-23", "天皇誕生日"], ["2026-03-20", "春分の日"], ["2026-04-29", "昭和の日"],
  ["2026-05-03", "憲法記念日"], ["2026-05-04", "みどりの日"], ["2026-05-05", "こどもの日"],
  ["2026-05-06", "振替休日"], ["2026-07-20", "海の日"], ["2026-08-11", "山の日"],
  ["2026-09-21", "敬老の日"], ["2026-09-22", "国民の休日"], ["2026-09-23", "秋分の日"],
  ["2026-10-12", "スポーツの日"], ["2026-11-03", "文化の日"], ["2026-11-23", "勤労感謝の日"],
  ["2027-01-01", "元日"], ["2027-01-11", "成人の日"], ["2027-02-11", "建国記念の日"],
  ["2027-02-23", "天皇誕生日"], ["2027-03-20", "春分の日"], ["2027-04-29", "昭和の日"],
  ["2027-05-03", "憲法記念日"], ["2027-05-04", "みどりの日"], ["2027-05-05", "こどもの日"],
  ["2027-07-19", "海の日"], ["2027-08-11", "山の日"], ["2027-09-20", "敬老の日"],
  ["2027-09-23", "秋分の日"], ["2027-10-11", "スポーツの日"], ["2027-11-03", "文化の日"],
  ["2027-11-23", "勤労感謝の日"]
];
function dayDiff(a, b) {
  const da = new Date(a.getFullYear(), a.getMonth(), a.getDate());
  const db = new Date(b.getFullYear(), b.getMonth(), b.getDate());
  return Math.round((db - da) / 86400000);
}
const now = new Date();
const widget = new ListWidget();
widget.backgroundColor = new Color("#2b1420");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🎌 次の祝日");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#f78fb8");
widget.addSpacer(10);
let found = null;
for (let i = 0; i < HOLIDAYS.length; i++) {
  const parts = HOLIDAYS[i][0].split("-");
  const d = new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
  if (dayDiff(now, d) >= 0) { found = [d, HOLIDAYS[i][1], dayDiff(now, d)]; break; }
}
if (found) {
  const name = widget.addText(found[1]);
  name.font = Font.boldSystemFont(24);
  name.textColor = new Color("#ffffff");
  widget.addSpacer(4);
  const WD = ["日", "月", "火", "水", "木", "金", "土"];
  const when = (found[0].getMonth() + 1) + "月" + found[0].getDate() + "日（" + WD[found[0].getDay()] + "）";
  const line = found[2] === 0 ? "今日です！ お休みを満喫しましょう" : when + " ・ あと " + found[2] + " 日";
  const sub = widget.addText(line);
  sub.font = Font.systemFont(14);
  sub.textColor = new Color(found[2] === 0 ? "#facc15" : "#d8cfc2");
} else {
  const end = widget.addText("収録分の祝日は終わりました");
  end.font = Font.systemFont(14);
  end.textColor = new Color("#d8cfc2");
}
Script.setWidget(widget);
""".trimIndent(),
        "🎂 誕生日カウント" to """
// プリセット: 誕生日カウント（大切な日まであと何日、ネット不要）
// ↓の名前と月日を書き換えて使う想定のテンプレ
const WHO = "大切な人";
const MONTH = 4, DAY = 29; // 例: 4月29日
function dayDiff(a, b) {
  const da = new Date(a.getFullYear(), a.getMonth(), a.getDate());
  const db = new Date(b.getFullYear(), b.getMonth(), b.getDate());
  return Math.round((db - da) / 86400000);
}
const now = new Date();
let target = new Date(now.getFullYear(), MONTH - 1, DAY);
if (dayDiff(now, target) < 0) target = new Date(now.getFullYear() + 1, MONTH - 1, DAY);
const left = dayDiff(now, target);
const widget = new ListWidget();
widget.backgroundColor = new Color("#2b1428");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🎂 " + WHO + "の誕生日");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#f78fb8");
widget.addSpacer(10);
if (left === 0) {
  const big = widget.addText("今日です！🎉");
  big.font = Font.boldSystemFont(30);
  big.textColor = new Color("#facc15");
} else {
  const row = widget.addStack();
  row.layoutHorizontally();
  row.alignItems = "center";
  const a = row.addText("あと ");
  a.font = Font.systemFont(16);
  a.textColor = new Color("#d8cfc2");
  const n = row.addText("" + left);
  n.font = Font.boldSystemFont(40);
  n.textColor = new Color("#ffffff");
  const b = row.addText(" 日");
  b.font = Font.systemFont(16);
  b.textColor = new Color("#d8cfc2");
}
widget.addSpacer(4);
const sub = widget.addText((target.getMonth() + 1) + "月" + target.getDate() + "日");
sub.font = Font.systemFont(12);
sub.textColor = new Color("#9d8fc7");
Script.setWidget(widget);
""".trimIndent(),
        "🐷 貯金メーター" to """
// プリセット: 貯金メーター（目標までの進み具合、ネット不要）
// ↓の3つを書き換えて使う想定のテンプレ。貯まったら NOW を更新するだけ
const LABEL = "旅行資金";
const GOAL = 300000;
const NOW = 126000;
function fmt(n) { return String(n).replace(/\B(?=(\d{3})+(?!\d))/g, ","); }
const pct = Math.min(1, NOW / GOAL);
const widget = new ListWidget();
widget.backgroundColor = new Color("#12262e");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🐷 貯金メーター");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#7cc7f0");
widget.addSpacer(10);
const name = widget.addText(LABEL);
name.font = Font.boldSystemFont(18);
name.textColor = new Color("#ffffff");
widget.addSpacer(6);
const holder = widget.addStack();
holder.addProgress(pct, new Color("#f78fb8"), new Color("#2c4350"));
widget.addSpacer(6);
const line = widget.addText("¥" + fmt(NOW) + " / ¥" + fmt(GOAL) + "（" + Math.floor(pct * 100) + "%）");
line.font = Font.systemFont(13);
line.textColor = new Color("#d8e8e8");
widget.addSpacer(2);
const left = widget.addText("ゴールまで あと ¥" + fmt(GOAL - NOW));
left.font = Font.systemFont(12);
left.textColor = new Color("#7d94b8");
Script.setWidget(widget);
""".trimIndent(),
        "💧 水分チャージ" to """
// プリセット: 水分チャージ（時刻から今日の水分の目安、ネット不要）
// 1日2Lを目安に、起きている時間で割った「いま何杯目くらいか」の目安
const now = new Date();
const h = now.getHours() + now.getMinutes() / 60;
const GOAL_CUPS = 8; // コップ8杯 ≒ 2L
let target = 0;
if (h >= 7 && h < 23) target = Math.min(GOAL_CUPS, Math.floor((h - 7) / 2) + 1);
else if (h >= 23) target = GOAL_CUPS;
const widget = new ListWidget();
widget.backgroundColor = new Color("#0e2436");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("💧 水分チャージ");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#7cc7f0");
widget.addSpacer(10);
let cups = "";
for (let i = 0; i < GOAL_CUPS; i++) cups += i < target ? "🟦" : "⬜";
const row = widget.addText(cups);
row.font = Font.systemFont(17);
widget.addSpacer(6);
const msg = h < 7 ? "おはようございます。朝の一杯から"
  : target >= GOAL_CUPS ? "今日の目安は達成ペースです"
  : "いま " + target + " 杯目のタイミングです";
const m = widget.addText(msg);
m.font = Font.boldSystemFont(15);
m.textColor = new Color("#ffffff");
widget.addSpacer(4);
const sub = widget.addText("目安: 1日コップ8杯（約2L）。のどが渇く前に少しずつ");
sub.font = Font.systemFont(11);
sub.textColor = new Color("#7d94b8");
Script.setWidget(widget);
""".trimIndent(),
        "🎋 毎日おみくじ" to """
// プリセット: 毎日おみくじ（日替わり運勢、ネット不要）
// 日付シードなので、その日は何度見ても同じ結果（引き直しは明日）
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
const rnd = mulberry32(seedNum * 29 + 7);
const FORTUNES = ["大吉", "中吉", "小吉", "吉", "末吉", "凶"];
const WEIGHTS = [12, 22, 24, 22, 14, 6];
const ITEMS = ["温かい飲み物", "青いハンカチ", "新しい靴下", "文房具", "観葉植物", "白いマグカップ", "イヤホン", "付箋", "ラムネ", "木のスプーン"];
const WORDS = ["思い切って先に謝ると丸く収まる日", "探し物は足元にあり", "いつもより一駅歩くと良いことあり", "温かい汁物が運を運ぶ", "後回しの1件を片付けると軽くなる", "笑顔の挨拶が効く日", "新しい道を通ると発見あり"];
let roll = rnd() * 100, acc = 0, fortune = "吉";
for (let i = 0; i < FORTUNES.length; i++) { acc += WEIGHTS[i]; if (roll < acc) { fortune = FORTUNES[i]; break; } }
const widget = new ListWidget();
widget.backgroundColor = new Color("#26161f");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🎋 毎日おみくじ");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#f78fb8");
widget.addSpacer(8);
const big = widget.addText(fortune);
big.font = Font.boldSystemFont(40);
big.textColor = new Color(fortune === "大吉" ? "#facc15" : fortune === "凶" ? "#9ca3af" : "#ffffff");
widget.addSpacer(6);
const w = widget.addText(WORDS[Math.floor(rnd() * WORDS.length)]);
w.font = Font.systemFont(13);
w.textColor = new Color("#e8d8e2");
widget.addSpacer(4);
const item = widget.addText("ラッキーアイテム: " + ITEMS[Math.floor(rnd() * ITEMS.length)]);
item.font = Font.systemFont(12);
item.textColor = new Color("#9d8fc7");
Script.setWidget(widget);
""".trimIndent(),
        "🎰 デイリーガチャ" to """
// プリセット: デイリーガチャ（1日1回気分のレア引き、ネット不要）
// 日替わり固定。星の数とアイテム名で小さく盛り上がる
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
const rnd = mulberry32(seedNum * 31 + 13);
const roll = rnd() * 100;
let stars, pool, col;
if (roll < 3) { stars = 5; col = "#facc15"; pool = ["伝説の剣（気分）", "虹色の羽根", "時の砂時計"]; }
else if (roll < 15) { stars = 4; col = "#ce9be8"; pool = ["魔法のコンパス", "星屑のペンダント", "賢者の石ころ"]; }
else if (roll < 45) { stars = 3; col = "#7cc7f0"; pool = ["鉄の盾", "回復ポーション", "革のブーツ", "銀の鍵"]; }
else { stars = 2; col = "#9ca3af"; pool = ["木の棒", "布の帽子", "石ころ", "ひも", "空き缶"]; }
const item = pool[Math.floor(rnd() * pool.length)];
let starStr = "";
for (let i = 0; i < 5; i++) starStr += i < stars ? "★" : "☆";
const widget = new ListWidget();
widget.backgroundColor = new Color("#1a1430");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🎰 デイリーガチャ");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ce9be8");
widget.addSpacer(10);
const s = widget.addText(starStr);
s.font = Font.boldSystemFont(26);
s.textColor = new Color(col);
widget.addSpacer(4);
const name = widget.addText(item);
name.font = Font.boldSystemFont(20);
name.textColor = new Color("#ffffff");
widget.addSpacer(4);
const sub = widget.addText(stars >= 4 ? "今日はついてる日かも" : "また明日、引き直せます");
sub.font = Font.systemFont(12);
sub.textColor = new Color("#9d8fc7");
Script.setWidget(widget);
""".trimIndent(),
        "🗡 レベル表示" to """
// プリセット: レベル表示（今年の経過日数＝レベル、ネット不要）
// 1日生きるごとにレベルが上がる。経験値バーは今年の進み具合
const now = new Date();
const start = new Date(now.getFullYear(), 0, 1);
const end = new Date(now.getFullYear() + 1, 0, 1);
const dayOfYear = Math.floor((new Date(now.getFullYear(), now.getMonth(), now.getDate()) - start) / 86400000) + 1;
const yearDays = Math.round((end - start) / 86400000);
const titles = ["駆け出し", "見習い", "一人前", "ベテラン", "達人", "賢者", "英雄", "伝説"];
const titleName = titles[Math.min(titles.length - 1, Math.floor(dayOfYear / 46))];
const widget = new ListWidget();
widget.backgroundColor = new Color("#141c2e");
widget.setPadding(14, 14, 14, 14);
const t = widget.addText("🗡 あなたのレベル");
t.font = Font.boldSystemFont(14);
t.textColor = new Color("#7cc7f0");
widget.addSpacer(8);
const row = widget.addStack();
row.layoutHorizontally();
row.alignItems = "center";
const lv = row.addText("Lv." + dayOfYear);
lv.font = Font.boldSystemFont(36);
lv.textColor = new Color("#ffffff");
row.addSpacer(10);
const tt = row.addText("称号: " + titleName);
tt.font = Font.boldSystemFont(15);
tt.textColor = new Color("#facc15");
widget.addSpacer(8);
const holder = widget.addStack();
holder.addProgress(dayOfYear / yearDays, new Color("#7cc7f0"), new Color("#2c3a55"));
widget.addSpacer(4);
const sub = widget.addText("次のレベルまで あと1日（寝れば上がります）");
sub.font = Font.systemFont(12);
sub.textColor = new Color("#8ea2c9");
Script.setWidget(widget);
""".trimIndent(),
        "👹 週末ボス" to """
// プリセット: 週末ボス（週末までの距離をボス戦っぽく、ネット不要）
// 平日はボスのHPが減っていく。土日は「討伐完了」
const now = new Date();
const day = now.getDay(); // 0=日 1=月 ... 6=土
const widget = new ListWidget();
widget.backgroundColor = new Color("#2b1215");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("👹 週末ボス");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#f87171");
widget.addSpacer(10);
if (day === 0 || day === 6) {
  const big = widget.addText("討伐完了！🏆");
  big.font = Font.boldSystemFont(28);
  big.textColor = new Color("#facc15");
  widget.addSpacer(4);
  const sub = widget.addText("今日は週末です。回復に専念しましょう");
  sub.font = Font.systemFont(13);
  sub.textColor = new Color("#e8d8d8");
} else {
  const hpLeft = (5 - day) / 4; // 金曜の終わりで0に近づく
  const big = widget.addText("ボスまで あと " + (6 - day) + " 日");
  big.font = Font.boldSystemFont(24);
  big.textColor = new Color("#ffffff");
  widget.addSpacer(8);
  const label = widget.addText("平日ボスのHP");
  label.font = Font.systemFont(11);
  label.textColor = new Color("#c9a0a0");
  widget.addSpacer(2);
  const holder = widget.addStack();
  holder.addProgress(Math.max(0.05, hpLeft), new Color("#f87171"), new Color("#4a2c2c"));
  widget.addSpacer(4);
  const sub = widget.addText(day === 5 ? "あと少し！ 今日を越えれば勝ちです" : "一歩ずつ削っていきましょう");
  sub.font = Font.systemFont(12);
  sub.textColor = new Color("#d8cfc2");
}
Script.setWidget(widget);
""".trimIndent(),
        "🍜 ランチ占い" to """
// プリセット: ランチ占い（今日のランチを日替わりで決定、ネット不要）
// 迷う時間を消す係。決められたら、あとは従うだけ
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
const rnd = mulberry32(seedNum * 37 + 17);
const MENU = [
  ["🍜", "ラーメン", "今日は麺の日。替え玉は自由"],
  ["🍛", "カレー", "スパイスで午後を乗り切る"],
  ["🍣", "寿司", "ちょっと贅沢にいく日"],
  ["🍔", "ハンバーガー", "がっつりいくなら今日"],
  ["🍝", "パスタ", "迷ったらトマト系"],
  ["🍱", "定食", "バランスよく、が正解の日"],
  ["🥗", "サラダ＋スープ", "軽めにして午後の眠気を回避"],
  ["🍙", "おにぎり", "軽く済ませて時間を買う日"],
  ["🍤", "天ぷら", "揚げたての音を聞きにいく"],
  ["🥪", "サンドイッチ", "片手で食べられる余裕を"],
  ["🍲", "鍋焼きうどん", "温かいもので整える日"],
  ["🌮", "タコス", "いつもと違う味に挑戦"]
];
const pick = MENU[Math.floor(rnd() * MENU.length)];
const widget = new ListWidget();
widget.backgroundColor = new Color("#2b1d10");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🍽 ランチ占い");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffab7a");
widget.addSpacer(10);
const big = widget.addText(pick[0] + " " + pick[1]);
big.font = Font.boldSystemFont(28);
big.textColor = new Color("#ffffff");
widget.addSpacer(4);
const sub = widget.addText(pick[2]);
sub.font = Font.systemFont(13);
sub.textColor = new Color("#e8dcc8");
widget.addSpacer(4);
const note = widget.addText("今日の分は決定済みです（占い）");
note.font = Font.systemFont(11);
note.textColor = new Color("#a08b6d");
Script.setWidget(widget);
""".trimIndent(),
        "🔮 今日イチ星座" to """
// プリセット: 今日イチ星座（12星座から日替わりで1位を発表、ネット不要）
// 自分の星座じゃなくても、1位の星座のラッキーカラーを借りる楽しみ方も
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
const rnd = mulberry32(seedNum * 41 + 19);
const SIGNS = ["牡羊座", "牡牛座", "双子座", "蟹座", "獅子座", "乙女座", "天秤座", "蠍座", "射手座", "山羊座", "水瓶座", "魚座"];
const COLORS = ["赤", "青", "黄色", "緑", "ピンク", "紫", "白", "黒", "金", "銀", "オレンジ", "水色"];
const ACTIONS = ["朝の散歩", "本を1ページ読む", "温かいお茶", "机の片付け", "早めの就寝", "ストレッチ", "手書きメモ", "深呼吸3回"];
const winner = SIGNS[Math.floor(rnd() * SIGNS.length)];
const widget = new ListWidget();
widget.backgroundColor = new Color("#161a30");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🔮 今日イチの星座");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ce9be8");
widget.addSpacer(10);
const big = widget.addText("✨ " + winner);
big.font = Font.boldSystemFont(30);
big.textColor = new Color("#ffffff");
widget.addSpacer(6);
const c = widget.addText("ラッキーカラー: " + COLORS[Math.floor(rnd() * COLORS.length)]);
c.font = Font.systemFont(13);
c.textColor = new Color("#e8e2f2");
widget.addSpacer(2);
const a = widget.addText("ラッキーアクション: " + ACTIONS[Math.floor(rnd() * ACTIONS.length)]);
a.font = Font.systemFont(13);
a.textColor = new Color("#e8e2f2");
Script.setWidget(widget);
""".trimIndent(),
        "🕐 でか時計" to """
// プリセット: でか時計（時刻を超大きく、ネット不要）
// 更新時点の時刻を表示する、最もシンプルな時計
function pad2(n) { return String(n).padStart(2, "0"); }
const WD = ["日", "月", "火", "水", "木", "金", "土"];
const now = new Date();
const widget = new ListWidget();
widget.backgroundColor = new Color("#101418");
widget.setPadding(14, 14, 14, 14);
const time = widget.addText(pad2(now.getHours()) + ":" + pad2(now.getMinutes()));
time.font = Font.boldSystemFont(52);
time.textColor = new Color("#ffffff");
widget.addSpacer(4);
const date = widget.addText((now.getMonth() + 1) + "月" + now.getDate() + "日（" + WD[now.getDay()] + "）");
date.font = Font.systemFont(15);
date.textColor = new Color("#9ca3af");
Script.setWidget(widget);
""".trimIndent(),
        "📅 今日の日付" to """
// プリセット: 今日の日付（月日と曜日だけを超大きく、ネット不要）
// 「今日何日だっけ」専用。これ以上は削れないシンプルさ
const WD = ["日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"];
const now = new Date();
const widget = new ListWidget();
widget.backgroundColor = new Color("#14181f");
widget.setPadding(14, 14, 14, 14);
const big = widget.addText((now.getMonth() + 1) + "月" + now.getDate() + "日");
big.font = Font.boldSystemFont(40);
big.textColor = new Color("#ffffff");
widget.addSpacer(4);
const wd = widget.addText(WD[now.getDay()]);
wd.font = Font.boldSystemFont(18);
wd.textColor = new Color(now.getDay() === 0 ? "#f87171" : now.getDay() === 6 ? "#7cc7f0" : "#9ca3af");
widget.addSpacer(4);
const yr = widget.addText(now.getFullYear() + "年");
yr.font = Font.systemFont(12);
yr.textColor = new Color("#6b7280");
Script.setWidget(widget);
""".trimIndent(),
        "📆 今週のマス" to """
// プリセット: 今週のマス（月〜日の7マス、ネット不要）
// 過ぎた日は塗られ、今日は光る。今週のどこにいるか一目で
const WD = ["月", "火", "水", "木", "金", "土", "日"];
const now = new Date();
const todayIdx = (now.getDay() + 6) % 7; // 月曜=0
const widget = new ListWidget();
widget.backgroundColor = new Color("#141c26");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("📆 今週の進み具合");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#7cc7f0");
widget.addSpacer(12);
const row = widget.addStack();
row.layoutHorizontally();
WD.forEach(function (w, i) {
  if (i > 0) row.addSpacer(6);
  const cell = row.addStack();
  cell.cornerRadius = 10;
  cell.setPadding(8, 10, 8, 10);
  cell.backgroundColor = new Color(i < todayIdx ? "#2c4a6b" : i === todayIdx ? "#f78fb8" : "#22303f");
  const t = cell.addText(w);
  t.font = Font.boldSystemFont(15);
  t.textColor = new Color(i === todayIdx ? "#1a0d14" : "#ffffff");
  t.align = "center";
});
widget.addSpacer(8);
const sub = widget.addText("今日は " + WD[todayIdx] + "曜日 ・ 今週 " + (todayIdx + 1) + " 日目");
sub.font = Font.systemFont(12);
sub.textColor = new Color("#8ea2c9");
Script.setWidget(widget);
""".trimIndent(),
        "📝 今日の一言" to """
// プリセット: 今日の一言（日替わりの短い言葉、ネット不要）
// 朝に目に入る、短くてやさしい一文だけ
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
const rnd = mulberry32(seedNum * 43 + 29);
const WORDS = [
  "今日は、ほどほどで十分",
  "休むのも仕事のうち",
  "小さく始めて、早く終わる",
  "深呼吸ひとつで、仕切り直し",
  "比べる相手は昨日の自分",
  "完璧より、完了",
  "温かいものを飲もう",
  "散歩は最強のリセット",
  "やらないことを決める日",
  "好きなものから手をつける",
  "返事は明日でも大丈夫",
  "空を見る余裕を",
  "今日の分は、今日の分だけ",
  "うまくいかない日も、進んでいる"
];
const pick = WORDS[Math.floor(rnd() * WORDS.length)];
const widget = new ListWidget();
widget.backgroundColor = new Color("#1c1a26");
widget.setPadding(16, 16, 16, 16);
const title = widget.addText("📝 今日の一言");
title.font = Font.boldSystemFont(13);
title.textColor = new Color("#ce9be8");
widget.addSpacer(10);
const big = widget.addText(pick);
big.font = Font.boldSystemFont(19);
big.textColor = new Color("#ffffff");
widget.addSpacer(6);
const sub = widget.addText((now.getMonth() + 1) + "/" + now.getDate() + " の言葉（毎日変わります）");
sub.font = Font.systemFont(11);
sub.textColor = new Color("#8f88a8");
Script.setWidget(widget);
""".trimIndent(),
        "🌿 深呼吸" to """
// プリセット: 深呼吸（円を見ながら呼吸を整える、ネット不要）
// 更新した瞬間の秒で円の大きさが決まる。4秒吸って、4秒吐く目安
const now = new Date();
const cycle = (now.getSeconds() % 8) / 8; // 8秒サイクル
const inhale = cycle < 0.5;
const t = inhale ? cycle * 2 : (cycle - 0.5) * 2;
const scale = inhale ? 0.45 + t * 0.55 : 1 - t * 0.55;
const widget = new ListWidget();
widget.backgroundColor = new Color("#0f2027");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🌿 深呼吸");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#86efac");
widget.addSpacer(8);
const S = 150, C = S / 2;
const cv = widget.addCanvas(S, S);
cv.rect(0, 0, S, S, "#0f2027");
cv.ring(C, C, 66, 1.2, 0, 360, "#2c4a4a");
cv.circle(C, C, 60 * scale, inhale ? "#5ec8a8" : "#3d8fa8");
cv.circle(C, C, 60 * scale * 0.55, inhale ? "#8fe3c3" : "#67b8cc");
widget.addSpacer(6);
const label = widget.addText(inhale ? "吸って…" : "吐いて…");
label.font = Font.boldSystemFont(16);
label.textColor = new Color("#ffffff");
const sub = widget.addText("円に合わせて、ゆっくり4秒ずつ");
sub.font = Font.systemFont(11);
sub.textColor = new Color("#7fae8f");
Script.setWidget(widget);
""".trimIndent(),
        "🌱 人生カウンタ" to """
// プリセット: 人生○日目（生まれてから何日目か、ネット不要）
// ↓の誕生日を自分のものに書き換えて使う想定のテンプレ
const BIRTH = "1998-11-11"; // 例: 1998年11月11日
function dayDiff(a, b) {
  const da = new Date(a.getFullYear(), a.getMonth(), a.getDate());
  const db = new Date(b.getFullYear(), b.getMonth(), b.getDate());
  return Math.round((db - da) / 86400000);
}
const parts = BIRTH.split("-");
const birth = new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
const now = new Date();
const days = dayDiff(birth, now) + 1;
const widget = new ListWidget();
widget.backgroundColor = new Color("#16241a");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🌱 人生カウンタ");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#86efac");
widget.addSpacer(10);
const row = widget.addStack();
row.layoutHorizontally();
row.alignItems = "center";
const pre = row.addText("今日は ");
pre.font = Font.systemFont(15);
pre.textColor = new Color("#d8e8d8");
const n = row.addText(String(days).replace(/\B(?=(\d{3})+(?!\d))/g, ","));
n.font = Font.boldSystemFont(34);
n.textColor = new Color("#ffffff");
const post = row.addText(" 日目");
post.font = Font.systemFont(15);
post.textColor = new Color("#d8e8d8");
widget.addSpacer(4);
const sub = widget.addText("1日1日が、ちゃんと積み上がっています");
sub.font = Font.systemFont(12);
sub.textColor = new Color("#7fae8f");
Script.setWidget(widget);
""".trimIndent(),
        "🏙 日替わりスカイライン" to """
// プリセット: 日替わりスカイライン（ビル群と窓明かりを生成、ネット不要）
// ビルの高さも窓の灯りも日付シード。毎晩ちがう街になる
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
const rnd = mulberry32(seedNum * 59 + 3);
const widget = new ListWidget();
widget.backgroundColor = new Color("#0b1026");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🏙 日替わりスカイライン");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);
const W = 232, H = 160, GROUND = H - 14;
const cv = widget.addCanvas(W, H);
cv.rect(0, 0, W, H, "#0b1026");
for (let i = 0; i < 34; i++) cv.circle((i * 67 + 13) % W, (i * 41 + 7) % 60, 1, "#46538a");
cv.circle(W - 34, 24, 11, "#fde68a");
cv.circle(W - 30, 21, 9, "#0b1026");
let x = 4;
while (x < W - 14) {
  const bw = 16 + Math.floor(rnd() * 18);
  const bh = 40 + rnd() * (GROUND - 56);
  const top = GROUND - bh;
  cv.rect(x, top, bw, bh, "#182238");
  const cols = Math.floor(bw / 6), rows = Math.floor(bh / 9);
  for (let r = 0; r < rows; r++) {
    for (let c = 0; c < cols; c++) {
      if (rnd() < 0.42) cv.rect(x + 3 + c * 6, top + 4 + r * 9, 3.4, 4.6, rnd() < 0.8 ? "#ffd97a" : "#a8d8ff");
    }
  }
  x += bw + 3;
}
cv.rect(0, GROUND, W, 2, "#26314d");
widget.addSpacer(6);
const foot = widget.addText((now.getMonth() + 1) + "/" + now.getDate() + " の街並み（毎日変わります）");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#7d8db0");
Script.setWidget(widget);
""".trimIndent(),
        "🌇 いまの空" to """
// プリセット: 夕日の空（時刻で色が変わるグラデーション、ネット不要）
// 朝は青、昼は明るく、夕方は橙、夜は紺。更新のたびに空が移る
const now = new Date();
const h = now.getHours() + now.getMinutes() / 60;
let stops, sunY, label;
if (h < 5 || h >= 19) { stops = ["#0b1026", "#1d2a52", "#3b3a6b"]; sunY = -30; label = "夜の空"; }
else if (h < 8) { stops = ["#7cc7f0", "#fbcfe8", "#fde68a"]; sunY = 96; label = "朝の空"; }
else if (h < 16) { stops = ["#38bdf8", "#7dd3fc", "#e0f2fb"]; sunY = 26; label = "昼の空"; }
else { stops = ["#312e81", "#c2410c", "#facc15"]; sunY = 92; label = "夕方の空"; }
const widget = new ListWidget();
widget.backgroundColor = new Color("#141021");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🌇 いまの空");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);
const W = 232, H = 140;
const cv = widget.addCanvas(W, H);
// 縦グラデーションを細い帯の重ねで表現
const BANDS = 28;
for (let i = 0; i < BANDS; i++) {
  const t = i / (BANDS - 1);
  const seg = t < 0.5 ? 0 : 1;
  const lt = t < 0.5 ? t * 2 : (t - 0.5) * 2;
  const c1 = stops[seg], c2 = stops[seg + 1];
  const mix = function (a, b) { return Math.round(a + (b - a) * lt); };
  const hex = function (s, o) { return parseInt(s.slice(o, o + 2), 16); };
  const r = mix(hex(c1, 1), hex(c2, 1)), g = mix(hex(c1, 3), hex(c2, 3)), b = mix(hex(c1, 5), hex(c2, 5));
  const col = "#" + [r, g, b].map(function (v) { return v.toString(16).padStart(2, "0"); }).join("");
  cv.rect(0, Math.floor(i * H / BANDS), W, Math.ceil(H / BANDS) + 1, col);
}
if (sunY > 0) {
  cv.circle(W / 2, sunY, 21, h >= 16 && h < 19 ? "#ff8a5c" : "#fde68a");
  cv.ring(W / 2, sunY, 27, 2, 0, 360, "#ffffff");
}
// 山影
cv.rect(0, H - 22, W, 22, "#10131f");
cv.circle(30, H - 6, 34, "#10131f");
cv.circle(120, H - 2, 46, "#10131f");
cv.circle(205, H - 8, 30, "#10131f");
widget.addSpacer(6);
const foot = widget.addText(label + "（時刻で色が変わります）");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#9d8fc7");
Script.setWidget(widget);
""".trimIndent(),
        "🌌 オーロラ" to """
// プリセット: オーロラ（波打つ光の帯を生成、ネット不要）
// サイン波の帯を何枚も重ねると、オーロラのカーテンに見えてくる
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
const rnd = mulberry32(seedNum * 61 + 7);
const widget = new ListWidget();
widget.backgroundColor = new Color("#060a18");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🌌 オーロラ");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);
const W = 232, H = 160;
const cv = widget.addCanvas(W, H);
cv.rect(0, 0, W, H, "#060a18");
for (let i = 0; i < 60; i++) cv.circle(rnd() * W, rnd() * H * 0.7, rnd() < 0.15 ? 1.7 : 1, "#54678f");
const cols = ["#2f9e6e", "#4ade80", "#67e8f9", "#a78bfa", "#4ade80", "#2f9e6e"];
for (let b = 0; b < 6; b++) {
  const base = 26 + b * 13, amp = 12 + rnd() * 14, fr = 0.02 + rnd() * 0.014, ph = rnd() * 6;
  let px = 0, py = base + Math.sin(ph) * amp;
  for (let x = 4; x <= W; x += 4) {
    const y = base + Math.sin(x * fr + ph) * amp + x * 0.06;
    cv.line(px, py, x, y, cols[b], 5.5);
    px = x; py = y;
  }
}
cv.rect(0, H - 16, W, 16, "#0a1220");
cv.circle(36, H - 4, 22, "#0a1220");
cv.circle(150, H - 2, 30, "#0a1220");
widget.addSpacer(6);
const foot = widget.addText("光のカーテンは日替わり");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#7d8db0");
Script.setWidget(widget);
""".trimIndent(),
        "🌱 今年の草" to """
// プリセット: 今年の草（GitHub風ヒートマップを日付シードで生成、ネット不要）
// 1日1マス。濃い緑が増えていく様子を眺めるだけの、小さな達成感
function mulberry32(a) {
  return function () {
    a |= 0; a = a + 0x6D2B79F5 | 0;
    let x = Math.imul(a ^ a >>> 15, 1 | a);
    x = x + Math.imul(x ^ x >>> 7, 61 | x) ^ x;
    return ((x ^ x >>> 14) >>> 0) / 4294967296;
  };
}
const now = new Date();
const start = new Date(now.getFullYear(), 0, 1);
const todayIdx = Math.floor((new Date(now.getFullYear(), now.getMonth(), now.getDate()) - start) / 86400000);
const LEVELS = ["#1d2b22", "#14532d", "#16a34a", "#4ade80", "#bbf7d0"];
const widget = new ListWidget();
widget.backgroundColor = new Color("#0d1512");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🌱 今年の草");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#86efac");
widget.addSpacer(8);
const CELL = 9, GAP = 2.6, COLS = 20;
const rows = Math.ceil((todayIdx + 1) / COLS);
const cv = widget.addCanvas(232, rows * (CELL + GAP) + 4);
cv.rect(0, 0, 232, rows * (CELL + GAP) + 4, "#0d1512");
for (let d = 0; d <= todayIdx; d++) {
  const rnd = mulberry32(now.getFullYear() * 1000 + d);
  const r = rnd();
  const lv = d === todayIdx ? 4 : r < 0.18 ? 0 : r < 0.45 ? 1 : r < 0.72 ? 2 : r < 0.92 ? 3 : 4;
  const x = 4 + (d % COLS) * (CELL + GAP), y = 2 + Math.floor(d / COLS) * (CELL + GAP);
  cv.roundRect(x, y, CELL, CELL, 2, LEVELS[lv]);
}
widget.addSpacer(6);
const foot = widget.addText((todayIdx + 1) + " 日分の草が生えました（今年の経過）");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#7fae8f");
Script.setWidget(widget);
""".trimIndent(),
        "🎭 日替わりフェイス" to """
// プリセット: 日替わりフェイス（顔パーツの組み合わせ生成、ネット不要）
// 目・口・ほっぺ・髪型を日替わりで組み合わせる、似顔絵ジェネレータ
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
const rnd = mulberry32(seedNum * 67 + 11);
const SKINS = ["#fde8d7", "#f5cfa8", "#d9a066", "#b07a45"];
const HAIRS = ["#2b2118", "#5b3a1e", "#8a8f98", "#c2410c", "#3b2f63", "#0f766e"];
const skin = SKINS[Math.floor(rnd() * SKINS.length)];
const hair = HAIRS[Math.floor(rnd() * HAIRS.length)];
const eyeType = Math.floor(rnd() * 3);
const mouthType = Math.floor(rnd() * 3);
const hairType = Math.floor(rnd() * 3);
const widget = new ListWidget();
widget.backgroundColor = new Color("#1c1a26");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🎭 日替わりフェイス");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);
const S = 170, C = S / 2;
const cv = widget.addCanvas(S, S);
cv.rect(0, 0, S, S, "#14121f");
cv.circle(C, C, 62, skin);
// 髪
if (hairType === 0) { cv.arc(C, C - 8, 63, 180, 180, hair); cv.rect(C - 63, C - 14, 126, 12, hair); }
else if (hairType === 1) { cv.arc(C, C - 4, 64, 170, 200, hair); cv.circle(C - 40, C - 48, 13, hair); cv.circle(C + 38, C - 50, 11, hair); }
else { cv.arc(C, C - 10, 62, 185, 170, hair); cv.circle(C, C - 66, 10, hair); }
// 目
const ey = C - 6;
if (eyeType === 0) { cv.circle(C - 22, ey, 5, "#241d16"); cv.circle(C + 22, ey, 5, "#241d16"); }
else if (eyeType === 1) { cv.arc(C - 22, ey + 3, 7, 180, 180, "#241d16"); cv.arc(C + 22, ey + 3, 7, 180, 180, "#241d16"); }
else { cv.line(C - 28, ey, C - 16, ey, "#241d16", 3); cv.line(C + 16, ey, C + 28, ey, "#241d16", 3); }
// 眉
cv.line(C - 29, ey - 14, C - 15, ey - 16, hair, 3);
cv.line(C + 15, ey - 16, C + 29, ey - 14, hair, 3);
// ほっぺ
cv.circle(C - 36, C + 18, 8, "#f9a8a8");
cv.circle(C + 36, C + 18, 8, "#f9a8a8");
// 口
const my = C + 26;
if (mouthType === 0) cv.arc(C, my - 4, 13, 20, 140, "#8a4b2a");
else if (mouthType === 1) { cv.circle(C, my + 2, 6, "#8a4b2a"); }
else cv.line(C - 10, my + 2, C + 10, my + 2, "#8a4b2a", 3);
widget.addSpacer(6);
const foot = widget.addText("今日の顔（明日は別人）");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#9d8fc7");
Script.setWidget(widget);
""".trimIndent(),
        "🖌 今日の川柳" to """
// プリセット: 川柳ジェネレータ（5・7・5を組み立て、ネット不要）
// 単語の組み合わせで、たまに名作が生まれる自動川柳
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
const rnd = mulberry32(seedNum * 71 + 13);
const FIVE_A = ["朝の駅", "昼休み", "帰り道", "布団の中", "雨の窓", "冷蔵庫", "給料日", "週末の", "深夜便", "散歩道"];
const FIVE_B = ["ため息が", "スマホ見て", "猫が来て", "コーヒーで", "あくびして", "月を見て", "風が吹き", "夢うつつ", "背伸びして", "時計見て"];
const SEVEN = ["気づけば今日も また同じ", "なんにもないが 腹は減る", "それでも今日は 悪くない", "明日の自分に 丸投げだ", "財布の中身は 雪景色", "言い訳だけが 上達し", "やる気の在庫 あと少し", "締め切りだけは 律儀だね", "小さな幸せ 拾い集め", "昨日の続きを 生きている"];
const FIVE_C = ["まあいいか", "なんとかなる", "今日も無事", "それでよし", "明日がある", "よく寝よう", "温まろう", "笑っとこ", "深呼吸", "上出来だ"];
function pick(arr) { return arr[Math.floor(rnd() * arr.length)]; }
const line1 = rnd() < 0.5 ? pick(FIVE_A) : pick(FIVE_B);
const line2 = pick(SEVEN);
const line3 = pick(FIVE_C);
const widget = new ListWidget();
widget.backgroundColor = new Color("#211a12");
widget.setPadding(16, 16, 16, 16);
const title = widget.addText("🖌 今日の川柳");
title.font = Font.boldSystemFont(13);
title.textColor = new Color("#facc15");
widget.addSpacer(10);
[line1, line2, line3].forEach(function (l, i) {
  if (i > 0) widget.addSpacer(4);
  const t = widget.addText(l);
  t.font = Font.boldSystemFont(19);
  t.textColor = new Color("#ffffff");
});
widget.addSpacer(8);
const foot = widget.addText("自動生成の一句（毎日差し替わります）");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#a08b6d");
Script.setWidget(widget);
""".trimIndent(),
        "🔳 QRもどき" to """
// プリセット: QRもどき（QRっぽい模様を生成、ネット不要）
// 3隅の目玉とランダムなマスで「それっぽさ」を再現。読み取れません
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
const rnd = mulberry32(seedNum * 73 + 17);
const widget = new ListWidget();
widget.backgroundColor = new Color("#16181d");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🔳 QRもどき");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);
const N = 21, CELL = 8, SIZE = N * CELL;
const cv = widget.addCanvas(232, 190);
cv.rect(0, 0, 232, 190, "#101216");
const OX = (232 - SIZE) / 2, OY = 10;
cv.rect(OX - 6, OY - 6, SIZE + 12, SIZE + 12, "#ffffff");
function inEye(x, y) {
  return (x < 8 && y < 8) || (x >= N - 8 && y < 8) || (x < 8 && y >= N - 8);
}
for (let y = 0; y < N; y++) {
  for (let x = 0; x < N; x++) {
    if (inEye(x, y)) continue;
    if (rnd() < 0.44) cv.rect(OX + x * CELL, OY + y * CELL, CELL, CELL, "#16181d");
  }
}
function eye(ex, ey) {
  cv.rect(OX + ex * CELL, OY + ey * CELL, 7 * CELL, 7 * CELL, "#16181d");
  cv.rect(OX + (ex + 1) * CELL, OY + (ey + 1) * CELL, 5 * CELL, 5 * CELL, "#ffffff");
  cv.rect(OX + (ex + 2) * CELL, OY + (ey + 2) * CELL, 3 * CELL, 3 * CELL, "#16181d");
}
eye(0, 0); eye(N - 7, 0); eye(0, N - 7);
widget.addSpacer(6);
const foot = widget.addText("読み取れません。雰囲気だけの模様です");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#8b93a5");
Script.setWidget(widget);
""".trimIndent(),
        "₿ ビットコイン" to """
// プリセット: ビットコイン価格（CoinGecko、本物のデータ）
// 円建て価格と24時間の動きだけを大きく
function fmt(n) { return String(Math.round(n)).replace(/\B(?=(\d{3})+(?!\d))/g, ","); }
const widget = new ListWidget();
widget.backgroundColor = new Color("#241a08");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("₿ ビットコイン");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#facc15");
widget.addSpacer(10);
try {
  const res = await fetch("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=jpy&include_24hr_change=true");
  const d = await res.json();
  const price = d.bitcoin.jpy, chg = d.bitcoin.jpy_24h_change;
  const big = widget.addText("¥" + fmt(price));
  big.font = Font.boldSystemFont(32);
  big.textColor = new Color("#ffffff");
  widget.addSpacer(4);
  const up = chg >= 0;
  const c = widget.addText("24時間で " + (up ? "▲ " : "▼ ") + Math.abs(chg).toFixed(1) + "%");
  c.font = Font.boldSystemFont(15);
  c.textColor = new Color(up ? "#86efac" : "#f87171");
  widget.addSpacer(4);
  const sub = widget.addText("出典: CoinGecko（投資助言ではありません）");
  sub.font = Font.systemFont(11);
  sub.textColor = new Color("#a08b6d");
} catch (e) {
  const err = widget.addText("価格を取得できませんでした");
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}
Script.setWidget(widget);
""".trimIndent(),
        "🥇 金価格の目安" to """
// プリセット: 金価格の目安（CoinGeckoの金連動トークン、本物のデータ）
// 1トロイオンスあたりの円換算。グラム単価も一緒に出す
function fmt(n) { return String(Math.round(n)).replace(/\B(?=(\d{3})+(?!\d))/g, ","); }
const widget = new ListWidget();
widget.backgroundColor = new Color("#26200c");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🥇 金価格の目安");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#facc15");
widget.addSpacer(10);
try {
  const res = await fetch("https://api.coingecko.com/api/v3/simple/price?ids=pax-gold&vs_currencies=jpy&include_24hr_change=true");
  const d = await res.json();
  const oz = d["pax-gold"].jpy, chg = d["pax-gold"].jpy_24h_change;
  const big = widget.addText("¥" + fmt(oz));
  big.font = Font.boldSystemFont(30);
  big.textColor = new Color("#ffffff");
  const unit = widget.addText("1トロイオンスあたり");
  unit.font = Font.systemFont(11);
  unit.textColor = new Color("#a08b6d");
  widget.addSpacer(4);
  const g = widget.addText("1g では約 ¥" + fmt(oz / 31.1035) + " ・ 24時間 " + (chg >= 0 ? "▲" : "▼") + " " + Math.abs(chg).toFixed(1) + "%");
  g.font = Font.systemFont(13);
  g.textColor = new Color("#e8e2d0");
  widget.addSpacer(2);
  const sub = widget.addText("金連動トークン（PAX Gold）経由の目安です");
  sub.font = Font.systemFont(10);
  sub.textColor = new Color("#8a8272");
} catch (e) {
  const err = widget.addText("価格を取得できませんでした");
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}
Script.setWidget(widget);
""".trimIndent(),
        "🐚 フィボナッチの渦" to """
// プリセット: フィボナッチの渦（黄金螺旋を計算で描く、ネット不要）
// 1,1,2,3,5,8…の正方形を並べると、自然界の渦が見えてくる
const widget = new ListWidget();
widget.backgroundColor = new Color("#101c1c");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🐚 フィボナッチの渦");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#7dd3c8");
widget.addSpacer(8);
const cv = widget.addCanvas(232, 176);
cv.rect(0, 0, 232, 176, "#0b1515");
// フィボナッチタイル配置（縮小しながら回転配置）
const unit = 3.1;
const fib = [1, 1, 2, 3, 5, 8, 13];
const cols = ["#134e4a", "#0f766e", "#2dd4bf", "#5eead4", "#99f6e4", "#ccfbf1", "#f0fdfa"];
let x = 116, y = 20, dir = 0; // 0:右 1:下 2:左 3:上 に正方形を足していく
const squares = [];
let minX = 1e9, minY = 1e9, maxX = -1e9, maxY = -1e9;
fib.forEach(function (f) {
  const s = f * unit;
  squares.push([x, y, s]);
  minX = Math.min(minX, x); minY = Math.min(minY, y);
  maxX = Math.max(maxX, x + s); maxY = Math.max(maxY, y + s);
  if (dir === 0) { x += s; y -= 0; }
  else if (dir === 1) { x -= 0; y += s; }
  else if (dir === 2) { x -= s; }
  else { y -= s; }
  dir = (dir + 1) % 4;
});
// 全体をキャンバス中央へ
const ox = (232 - (maxX - minX)) / 2 - minX, oy = (176 - (maxY - minY)) / 2 - minY;
squares.forEach(function (sq, i) {
  cv.rect(sq[0] + ox, sq[1] + oy, sq[2], sq[2], cols[i % cols.length]);
});
// 螺旋：各正方形に四分円
squares.forEach(function (sq, i) {
  const cx = sq[0] + ox + (i % 2 === 0 ? sq[2] : 0), cy = sq[1] + oy + (i % 3 === 0 ? sq[2] : 0);
  cv.arc(cx, cy, sq[2], (i * 90) % 360, 90, "#0b1515");
});
widget.addSpacer(6);
const foot = widget.addText("1, 1, 2, 3, 5, 8, 13… の並びが描く渦");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#6d9a94");
Script.setWidget(widget);
""".trimIndent(),
        "💴 次の給料日" to """
// プリセット: 次の給料日（ネット不要）
// 給料日は毎月25日想定。違う人は下の PAYDAY を書き換えて
const PAYDAY = 25;
const now = new Date();
const y = now.getFullYear(), m = now.getMonth(), d = now.getDate();
let next = new Date(y, m, PAYDAY);
if (d > PAYDAY || (d === PAYDAY && now.getHours() >= 12)) next = new Date(y, m + 1, PAYDAY);
if (d === PAYDAY && now.getHours() < 12) next = new Date(y, m, PAYDAY);
const days = Math.round((new Date(next.getFullYear(), next.getMonth(), next.getDate()) - new Date(y, m, d)) / 86400000);
const widget = new ListWidget();
widget.backgroundColor = new Color("#132117");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("💴 次の給料日");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#86efac");
widget.addSpacer(10);
if (days === 0) {
  const big = widget.addText("今日です！");
  big.font = Font.boldSystemFont(30);
  big.textColor = new Color("#ffffff");
} else {
  const big = widget.addText("あと " + days + " 日");
  big.font = Font.boldSystemFont(30);
  big.textColor = new Color("#ffffff");
  const sub = widget.addText((next.getMonth() + 1) + "月" + next.getDate() + "日（毎月" + PAYDAY + "日想定）");
  sub.font = Font.systemFont(12);
  sub.textColor = new Color("#7fae8f");
}
widget.addSpacer(4);
const passed = Math.min(d, PAYDAY);
const row = widget.addStack();
row.layoutHorizontally();
row.addProgress(passed / PAYDAY, "#4ade80", "#1d2b22");
Script.setWidget(widget);
""".trimIndent(),
        "🧘 日替わりストレッチ" to """
// プリセット: 日替わりストレッチ（ネット不要）
// 曜日ごとに部位が回る。無理のない範囲でどうぞ
const MENU = [
  { part: "首と肩", moves: ["首をゆっくり左右に倒す ×3", "肩を大きく回す 前後×5", "肩甲骨を寄せて5秒 ×3"], color: "#7cc7f0" },
  { part: "背中", moves: ["両手を組んで前へ伸ばす 15秒", "椅子に座って体をひねる 左右", "猫のポーズで背中を丸める ×3"], color: "#ce9be8" },
  { part: "腰", moves: ["仰向けで両膝を抱える 20秒", "膝を左右に倒す ゆっくり×4", "立って腰を回す 左右×5"], color: "#ffab7a" },
  { part: "脚", moves: ["太もも前を伸ばす 左右20秒", "ふくらはぎ伸ばし 左右20秒", "足首を回す 左右×8"], color: "#86efac" },
  { part: "胸と腕", moves: ["壁に手をついて胸を開く 15秒", "腕を胸の前で引く 左右15秒", "手首を反らす・曲げる ×5"], color: "#f78fb8" },
  { part: "全身", moves: ["深呼吸しながら背伸び ×3", "前屈してだらんと揺れる 20秒", "その場で軽くジャンプ ×10"], color: "#facc15" },
  { part: "目と顔", moves: ["目をぎゅっと閉じて開く ×5", "遠くと近くを交互に見る ×5", "口を大きく開けて閉じる ×5"], color: "#7dd3c8" },
];
const now = new Date();
const item = MENU[now.getDay() % MENU.length];
const widget = new ListWidget();
widget.backgroundColor = new Color("#14181f");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🧘 今日のストレッチ");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(6);
const part = widget.addText("今日は「" + item.part + "」の日");
part.font = Font.boldSystemFont(17);
part.textColor = new Color(item.color);
item.moves.forEach(function (mv) {
  widget.addSpacer(4);
  const t = widget.addText("・" + mv);
  t.font = Font.systemFont(13);
  t.textColor = new Color("#d5dbe7");
});
widget.addSpacer(4);
const foot = widget.addText("痛みがあるときは無理しないでください");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#8b93a5");
Script.setWidget(widget);
""".trimIndent(),
        "🔤 今日の英単語" to """
// プリセット: 今日の英単語（ネット不要）
// 1日1語。例文つきで、眺めるだけでも少しずつ増えていく
const WORDS = [
  ["serendipity", "思いがけない幸運", "Finding that café was pure serendipity."],
  ["cozy", "居心地のいい", "This room is small but cozy."],
  ["wander", "ぶらぶら歩く", "We wandered around the old town."],
  ["breeze", "そよ風", "A cool breeze came from the sea."],
  ["sparkle", "きらめく", "Her eyes sparkle when she talks about music."],
  ["mellow", "まろやかな、落ち着いた", "The mellow light of evening filled the room."],
  ["curious", "好奇心が強い", "He is curious about everything."],
  ["gentle", "やさしい、穏やかな", "She has a gentle voice."],
  ["brave", "勇敢な", "It was brave of you to speak up."],
  ["harvest", "収穫", "This year's rice harvest was good."],
  ["whisper", "ささやく", "She whispered a secret to me."],
  ["journey", "旅", "The journey took three days."],
  ["bloom", "花が咲く", "Cherry trees bloom in spring."],
  ["courage", "勇気", "It takes courage to start over."],
  ["humble", "謙虚な", "He stayed humble after his success."],
];
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
const item = WORDS[Math.floor(mulberry32(seedNum)() * WORDS.length)];
const widget = new ListWidget();
widget.backgroundColor = new Color("#0f2233");
widget.setPadding(16, 16, 16, 16);
const title = widget.addText("🔤 今日の英単語");
title.font = Font.boldSystemFont(13);
title.textColor = new Color("#7cc7f0");
widget.addSpacer(8);
const w = widget.addText(item[0]);
w.font = Font.boldSystemFont(30);
w.textColor = new Color("#ffffff");
const mean = widget.addText(item[1]);
mean.font = Font.systemFont(14);
mean.textColor = new Color("#bae6fd");
widget.addSpacer(6);
const ex = widget.addText(item[2]);
ex.font = Font.systemFont(12);
ex.textColor = new Color("#8fb8d8");
Script.setWidget(widget);
""".trimIndent(),
        "🈳 今日の漢字" to """
// プリセット: 今日の漢字（ネット不要）
// 1日1字。読みと意味と、ちょっとした成り立ちメモつき
const KANJI = [
  ["凪", "なぎ", "風がやんで波が静まること", "止まる風と書いて、凪"] ,
  ["灯", "あかり／ひ", "明かり、ともしび", "火に丁（打つ）で灯す火"],
  ["結", "むすぶ", "つなぐ、まとまる", "糸を吉く結ぶ"],
  ["奏", "かなでる", "音楽を演奏する", "両手で天に奉る形から"],
  ["縁", "えん", "つながり、めぐり合わせ", "糸に彖（縁のふち）"],
  ["翔", "かける", "高く飛ぶ", "羊の羽と書く、飛翔の翔"],
  ["凛", "りん", "引き締まって清い", "冫（氷）に稟（授かる）"],
  ["紡", "つむぐ", "糸を作る、言葉を紡ぐ", "糸に方（並べる）"],
  ["澄", "すむ", "清く濁りがない", "水に登る、澄み渡る"],
  ["温", "あたたかい", "ぬくもり", "水に囚われず、日を浴びる器"],
  ["綾", "あや", "模様のある織物、もつれ", "糸に夌（入り組む）"],
  ["響", "ひびく", "音が広がる", "郷に音、響き合う"],
  ["漕", "こぐ", "舟を進める", "水に曹（つらなる）"],
  ["憩", "いこい", "休むこと", "舌と自と心、息をつく"],
  ["萌", "もえ", "芽が出る、新しい始まり", "草に明、芽吹きの光"],
];
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
const item = KANJI[Math.floor(mulberry32(seedNum * 3)() * KANJI.length)];
const widget = new ListWidget();
widget.backgroundColor = new Color("#1d1626");
widget.setPadding(16, 16, 16, 16);
const title = widget.addText("🈳 今日の漢字");
title.font = Font.boldSystemFont(13);
title.textColor = new Color("#ce9be8");
const row = widget.addStack();
row.layoutHorizontally();
const big = row.addText(item[0]);
big.font = Font.boldSystemFont(56);
big.textColor = new Color("#ffffff");
row.addSpacer(14);
const col = row.addStack();
col.addSpacer(8);
const yomi = col.addText(item[1]);
yomi.font = Font.boldSystemFont(16);
yomi.textColor = new Color("#e9d5ff");
const mean = col.addText(item[2]);
mean.font = Font.systemFont(13);
mean.textColor = new Color("#cfc2e8");
widget.addSpacer(4);
const memo = widget.addText(item[3]);
memo.font = Font.systemFont(11);
memo.textColor = new Color("#9d8fc7");
Script.setWidget(widget);
""".trimIndent(),
        "🧾 割り勘計算" to """
// プリセット: 割り勘計算（ネット不要）
// ↓ 金額と人数を書き換えて使ってください
const TOTAL = 12800;  // 合計金額（円）
const PEOPLE = 4;     // 人数
function fmt(n) { return String(Math.round(n)).replace(/\B(?=(\d{3})+(?!\d))/g, ","); }
const per = TOTAL / PEOPLE;
const perRound = Math.ceil(per / 100) * 100; // 100円単位で切り上げた集金額
const collected = perRound * PEOPLE;
const widget = new ListWidget();
widget.backgroundColor = new Color("#132117");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🧾 割り勘");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#86efac");
widget.addSpacer(8);
const info = widget.addText("合計 ¥" + fmt(TOTAL) + " を " + PEOPLE + " 人で");
info.font = Font.systemFont(13);
info.textColor = new Color("#c9e8d2");
widget.addSpacer(2);
const big = widget.addText("1人 ¥" + fmt(per));
big.font = Font.boldSystemFont(28);
big.textColor = new Color("#ffffff");
widget.addSpacer(4);
const tipText = collected > TOTAL
  ? "キリよく ¥" + fmt(perRound) + " ずつ集めると、幹事に ¥" + fmt(collected - TOTAL) + " 残ります"
  : "割り切れるので、ぴったり集まります";
const tip = widget.addText(tipText);
tip.font = Font.systemFont(12);
tip.textColor = new Color("#7fae8f");
Script.setWidget(widget);
""".trimIndent(),
        "⚖️ BMIメーター" to """
// プリセット: BMIメーター（ネット不要）
// ↓ 身長と体重を書き換えて使ってください
const HEIGHT_CM = 170;
const WEIGHT_KG = 74;
const m = HEIGHT_CM / 100;
const bmi = WEIGHT_KG / (m * m);
const target = 22 * m * m; // BMI 22 の体重
let label, color;
if (bmi < 18.5) { label = "やせ型"; color = "#7cc7f0"; }
else if (bmi < 25) { label = "普通体重"; color = "#86efac"; }
else if (bmi < 30) { label = "肥満（1度）"; color = "#ffab7a"; }
else { label = "肥満（2度以上）"; color = "#f87171"; }
const widget = new ListWidget();
widget.backgroundColor = new Color("#141a24");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("⚖️ BMIメーター");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);
const big = widget.addText(bmi.toFixed(1));
big.font = Font.boldSystemFont(36);
big.textColor = new Color(color);
const lab = widget.addText(label + "（" + HEIGHT_CM + "cm / " + WEIGHT_KG + "kg）");
lab.font = Font.systemFont(13);
lab.textColor = new Color("#c3ccd9");
widget.addSpacer(6);
// 15〜35 をゲージにする
const row = widget.addStack();
row.layoutHorizontally();
row.addProgress(Math.max(0, Math.min(1, (bmi - 15) / 20)), color, "#232c3d");
widget.addSpacer(4);
const foot = widget.addText("BMI 22 の目安体重は約 " + target.toFixed(1) + " kg");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#8b93a5");
Script.setWidget(widget);
""".trimIndent(),
        "📚 読書メーター" to """
// プリセット: 読書メーター（ネット不要）
// ↓ 本の総ページと、いま読んだページを書き換えて
const TOTAL_PAGES = 320;
const NOW_PAGE = 128;
const pct = Math.max(0, Math.min(1, NOW_PAGE / TOTAL_PAGES));
const left = TOTAL_PAGES - NOW_PAGE;
const widget = new ListWidget();
widget.backgroundColor = new Color("#201812");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("📚 読書メーター");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffcf9e");
widget.addSpacer(8);
const big = widget.addText(Math.round(pct * 100) + "%");
big.font = Font.boldSystemFont(34);
big.textColor = new Color("#ffffff");
const info = widget.addText(NOW_PAGE + " / " + TOTAL_PAGES + " ページ（あと " + left + " ページ）");
info.font = Font.systemFont(13);
info.textColor = new Color("#d8c6ae");
widget.addSpacer(6);
const row = widget.addStack();
row.layoutHorizontally();
row.addProgress(pct, "#f59e0b", "#3a2d1d");
widget.addSpacer(4);
const per = Math.ceil(left / 14);
const foot = widget.addText("1日 " + per + " ページで2週間で読了ペース");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#a08b6d");
Script.setWidget(widget);
""".trimIndent(),
        "🥵 不快指数" to """
// プリセット: 不快指数（Open-Meteo、東京の本物のデータ）
// 気温と湿度から不快指数を計算。数字の目安つき
const LAT = 35.6762, LON = 139.6503; // 東京
const widget = new ListWidget();
widget.backgroundColor = new Color("#241408");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🥵 不快指数（東京）");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffab7a");
widget.addSpacer(8);
try {
  const url = "https://api.open-meteo.com/v1/forecast?latitude=" + LAT + "&longitude=" + LON + "&current=temperature_2m,relative_humidity_2m&timezone=Asia%2FTokyo";
  const res = await fetch(url);
  const d = await res.json();
  const t = d.current.temperature_2m, rh = d.current.relative_humidity_2m;
  // 不快指数 = 0.81T + 0.01H(0.99T − 14.3) + 46.3
  const di = 0.81 * t + 0.01 * rh * (0.99 * t - 14.3) + 46.3;
  let label, color;
  if (di < 55) { label = "寒く感じる"; color = "#7cc7f0"; }
  else if (di < 60) { label = "肌寒い"; color = "#7cc7f0"; }
  else if (di < 65) { label = "何も感じない"; color = "#86efac"; }
  else if (di < 70) { label = "快適"; color = "#86efac"; }
  else if (di < 75) { label = "不快に感じ始める"; color = "#facc15"; }
  else if (di < 80) { label = "やや暑く不快"; color = "#ffab7a"; }
  else if (di < 85) { label = "暑くて汗が出る"; color = "#fb923c"; }
  else { label = "暑くてたまらない"; color = "#f87171"; }
  const big = widget.addText(di.toFixed(0));
  big.font = Font.boldSystemFont(40);
  big.textColor = new Color(color);
  const lab = widget.addText(label);
  lab.font = Font.boldSystemFont(16);
  lab.textColor = new Color("#ffffff");
  widget.addSpacer(4);
  const sub = widget.addText("気温 " + t.toFixed(1) + "℃ ・ 湿度 " + Math.round(rh) + "%");
  sub.font = Font.systemFont(12);
  sub.textColor = new Color("#c9b8a6");
} catch (e) {
  const err = widget.addText("データを取得できませんでした");
  err.font = Font.systemFont(13);
  err.textColor = new Color("#ffab7a");
}
Script.setWidget(widget);
""".trimIndent(),
        "🍳 今日の献立" to """
// プリセット: 今日の献立（ネット不要）
// 主菜＋副菜＋汁物を日替わりで組み合わせる、夕食の迷い解消
const MAINS = ["鶏の照り焼き", "鮭の塩焼き", "豚の生姜焼き", "麻婆豆腐", "鶏の唐揚げ", "サバの味噌煮", "ハンバーグ", "肉じゃが", "エビチリ", "親子丼", "豚キムチ炒め", "ブリの照り焼き", "カレイの煮付け", "鶏の南蛮漬け", "回鍋肉"];
const SIDES = ["ほうれん草のおひたし", "きんぴらごぼう", "ポテトサラダ", "冷奴", "もやしナムル", "ひじきの煮物", "トマトのマリネ", "ブロッコリーの胡麻和え", "切り干し大根", "ピーマンの塩昆布和え", "なすの揚げ浸し", "かぼちゃの煮物"];
const SOUPS = ["わかめと豆腐の味噌汁", "大根と油揚げの味噌汁", "玉ねぎのコンソメスープ", "豚汁", "けんちん汁", "卵スープ", "あさりの味噌汁", "野菜たっぷりミネストローネ", "もずくスープ", "なめこの味噌汁"];
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
const rnd = mulberry32(seedNum * 79 + 19);
function pick(arr) { return arr[Math.floor(rnd() * arr.length)]; }
const widget = new ListWidget();
widget.backgroundColor = new Color("#20130d");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🍳 今日の献立");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffcf9e");
widget.addSpacer(8);
[["主菜", pick(MAINS)], ["副菜", pick(SIDES)], ["汁物", pick(SOUPS)]].forEach(function (pair, i) {
  if (i > 0) widget.addSpacer(4);
  const t = widget.addText(pair[0] + "　" + pair[1]);
  t.font = i === 0 ? Font.boldSystemFont(17) : Font.systemFont(14);
  t.textColor = new Color(i === 0 ? "#ffffff" : "#e8d9c6");
});
widget.addSpacer(6);
const foot = widget.addText("迷ったらこれ。毎日組み合わせが変わります");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#a08b6d");
Script.setWidget(widget);
""".trimIndent(),
        "🧹 今日の掃除スポット" to """
// プリセット: 今日の掃除スポット（ネット不要）
// 1日1か所だけ。全部やらない、今日の分だけ
const SPOTS = [
  { name: "玄関", tip: "靴を全部出して、たたきを拭く" },
  { name: "キッチンのシンク", tip: "スポンジで磨いて水気を拭き取る" },
  { name: "洗面台", tip: "鏡と蛇口を拭くだけで印象が変わる" },
  { name: "トイレ", tip: "便座の裏と床をさっと拭く" },
  { name: "お風呂の排水口", tip: "髪を取り、ぬめりを落とす" },
  { name: "冷蔵庫の野菜室", tip: "古いものを出して拭き掃除" },
  { name: "デスク周り", tip: "ケーブルとモニタ裏のほこり" },
  { name: "窓ガラス1枚", tip: "今日は1枚だけ、濡れ拭き→乾拭き" },
  { name: "クローゼットの床", tip: "掃除機をかけて風を通す" },
  { name: "ゴミ箱", tip: "中を洗って日光に当てる" },
  { name: "エアコンのフィルター", tip: "外して水洗い（よく乾かす）" },
  { name: "靴箱", tip: "履かない靴を1足手放す" },
  { name: "ベランダ", tip: "排水口の落ち葉を取る" },
  { name: "カーテン", tip: "洗濯機で洗う（ネット使用）" },
];
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
const item = SPOTS[Math.floor(mulberry32(seedNum * 5)() * SPOTS.length)];
const widget = new ListWidget();
widget.backgroundColor = new Color("#0f2027");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🧹 今日の掃除スポット");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#7dd3fc");
widget.addSpacer(8);
const big = widget.addText(item.name);
big.font = Font.boldSystemFont(22);
big.textColor = new Color("#ffffff");
widget.addSpacer(4);
const tip = widget.addText(item.tip);
tip.font = Font.systemFont(13);
tip.textColor = new Color("#c3dbe8");
widget.addSpacer(4);
const foot = widget.addText("今日はここだけ。5分で十分です");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#7fa3b5");
Script.setWidget(widget);
""".trimIndent(),
        "✊ 今日のじゃんけん" to """
// プリセット: 今日のじゃんけん（ネット不要）
// 今日の「出す手」は決まっている。勝ちたければ、あいこの手を…？
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
const idx = Math.floor(mulberry32(seedNum * 83)() * 3);
const HANDS = [
  { emoji: "✊", name: "グー", win: "✌ チョキ", lose: "✋ パー" },
  { emoji: "✌", name: "チョキ", win: "✋ パー", lose: "✊ グー" },
  { emoji: "✋", name: "パー", win: "✊ グー", lose: "✌ チョキ" },
];
const h = HANDS[idx];
const widget = new ListWidget();
widget.backgroundColor = new Color("#1d1a10");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("✊ 今日のじゃんけん");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#facc15");
widget.addSpacer(6);
const big = widget.addText(h.emoji + " " + h.name);
big.font = Font.boldSystemFont(34);
big.textColor = new Color("#ffffff");
widget.addSpacer(4);
const info = widget.addText("今日の相手はこれを出します。\n勝つなら " + h.win + "、負けるなら " + h.lose + "。");
info.font = Font.systemFont(13);
info.textColor = new Color("#d8cf9e");
Script.setWidget(widget);
""".trimIndent(),
        "🎲 サイコロ" to """
// プリセット: サイコロ（ネット不要）
// 更新された瞬間の「秒」で目が決まる。振り直しは手動更新で
const now = new Date();
const face = (now.getSeconds() % 6) + 1;
const PIPS = {
  1: [[1, 1]],
  2: [[0, 0], [2, 2]],
  3: [[0, 0], [1, 1], [2, 2]],
  4: [[0, 0], [0, 2], [2, 0], [2, 2]],
  5: [[0, 0], [0, 2], [1, 1], [2, 0], [2, 2]],
  6: [[0, 0], [0, 1], [0, 2], [2, 0], [2, 1], [2, 2]],
};
const widget = new ListWidget();
widget.backgroundColor = new Color("#17171f");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🎲 サイコロ");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);
const S = 120, CELL = 34, OFF = (S - CELL * 3) / 2 + CELL / 2;
const cv = widget.addCanvas(S, S);
cv.roundRect(6, 6, S - 12, S - 12, 18, "#f4f1ea");
PIPS[face].forEach(function (p) {
  cv.circle(OFF + p[0] * CELL, OFF + p[1] * CELL, 9, face === 1 ? "#dc2626" : "#26262e");
});
widget.addSpacer(6);
const foot = widget.addText("出た目は " + face + "（更新のたびに振り直し）");
foot.font = Font.systemFont(12);
foot.textColor = new Color("#9a9aad");
Script.setWidget(widget);
""".trimIndent(),
        "🎯 当番ルーレット" to """
// プリセット: 当番ルーレット（ネット不要）
// ↓ 名前リストを書き換えて。今日の当番は日替わりで決まります
const NAMES = ["Aさん", "Bさん", "Cさん", "Dさん"];
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
const idx = Math.floor(mulberry32(seedNum * 89)() * NAMES.length);
const widget = new ListWidget();
widget.backgroundColor = new Color("#241028");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🎯 今日の当番");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#f0a6ff");
widget.addSpacer(8);
NAMES.forEach(function (n, i) {
  if (i > 0) widget.addSpacer(2);
  const t = widget.addText((i === idx ? "▶ " : "　 ") + n);
  t.font = i === idx ? Font.boldSystemFont(19) : Font.systemFont(14);
  t.textColor = new Color(i === idx ? "#ffffff" : "#8f7ba3");
});
widget.addSpacer(4);
const foot = widget.addText("リストはスクリプトを書き換えて変更できます");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#8f7ba3");
Script.setWidget(widget);
""".trimIndent(),
        "💬 今日の迷言" to """
// プリセット: 迷言ジェネレータ（ネット不要）
// それっぽい格言を自動生成する。深いようで深くない
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
const rnd = mulberry32(seedNum * 97 + 23);
const A = ["人生は", "成功は", "幸せは", "時間は", "努力は", "未来は", "仕事は", "眠気は"];
const B = ["待つ者に", "逃げる者に", "笑う者に", "急ぐ者に", "眠る者に", "食べる者に", "考える者に", "動く者に"];
const C = ["微笑む", "味方する", "やってくる", "逃げていく", "応えてくれる", "試してくる", "寄り添う", "牙をむく"];
const D = ["ただし布団の中では例外である", "なお在庫に限りがある", "個人の感想です", "効果には個人差がある", "ただし締め切りは別である", "だいたい当たっている", "出典は不明である", ""];
function pick(arr) { return arr[Math.floor(rnd() * arr.length)]; }
const line = pick(A) + pick(B) + pick(C) + "。";
const tail = pick(D);
const widget = new ListWidget();
widget.backgroundColor = new Color("#101820");
widget.setPadding(16, 16, 16, 16);
const title = widget.addText("💬 今日の迷言");
title.font = Font.boldSystemFont(13);
title.textColor = new Color("#7cc7f0");
widget.addSpacer(10);
const big = widget.addText("「" + line + "」");
big.font = Font.boldSystemFont(17);
big.textColor = new Color("#ffffff");
if (tail) {
  widget.addSpacer(4);
  const t = widget.addText("※ " + tail);
  t.font = Font.systemFont(12);
  t.textColor = new Color("#8fa8bf");
}
widget.addSpacer(4);
const foot = widget.addText("自動生成のことわざ風テキストです");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#5f7488");
Script.setWidget(widget);
""".trimIndent(),
        "🪐 惑星年齢" to """
// プリセット: 惑星年齢（ネット不要）
// ↓ 誕生日を書き換えて。惑星ごとに「何歳か」が変わる
const BIRTH = "1990-04-01";
const parts = BIRTH.split("-");
const birth = new Date(+parts[0], +parts[1] - 1, +parts[2]);
const earthYears = (Date.now() - birth.getTime()) / (365.25 * 86400000);
const PLANETS = [
  ["☿ 水星", 0.241],
  ["♀ 金星", 0.615],
  ["♂ 火星", 1.881],
  ["♃ 木星", 11.86],
  ["♄ 土星", 29.45],
];
const widget = new ListWidget();
widget.backgroundColor = new Color("#101626");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🪐 惑星では何歳？");
title.font = Font.boldSystemFont(14);
title.textColor = new Color("#a5b8f0");
widget.addSpacer(6);
const earth = widget.addText("地球では " + earthYears.toFixed(1) + " 歳");
earth.font = Font.boldSystemFont(17);
earth.textColor = new Color("#ffffff");
PLANETS.forEach(function (p) {
  widget.addSpacer(2);
  const t = widget.addText(p[0] + " では " + (earthYears / p[1]).toFixed(p[1] > 10 ? 2 : 1) + " 歳");
  t.font = Font.systemFont(13);
  t.textColor = new Color("#c3cdea");
});
widget.addSpacer(2);
const foot = widget.addText("公転周期で割っただけの楽しい換算です");
foot.font = Font.systemFont(11);
foot.textColor = new Color("#71809f");
Script.setWidget(widget);
""".trimIndent(),
        "🐱 猫の一言" to """
// プリセット: 猫の一言（ネット不要）
// 猫っぽい短文と顔文字。意味はないが和む
function mulberry32(a) {
  return function () {
    a |= 0; a = a + 0x6D2B79F5 | 0;
    let x = Math.imul(a ^ a >>> 15, 1 | a);
    x = x + Math.imul(x ^ x >>> 7, 61 | x) ^ x;
    return ((x ^ x >>> 14) >>> 0) / 4294967296;
  };
}
const LINES = [
  ["今日は寝る日", "(=˘ω˘=)"],
  ["ごはんはまだか", "(=・ω・=)"],
  ["その箱、入る", "(=^･ω･^=)"],
  ["かまってほしい", "(=;ω;=)"],
  ["日向ぼっこ中", "(=^-ω-^=)"],
  ["なでてもいいよ", "(=ﾉωﾉ=)"],
  ["夜はこれから", "(=ΦωΦ=)"],
  ["もう眠い", "(=-ω-=)"],
  ["遊んでほしい", "(=oωo=)"],
  ["満足した", "(=˘ω˘=) zzZ"],
  ["見ているぞ", "(=・ω・)じー"],
  ["雨の日は寝る", "(=˘ω˘=)"],
];
const now = new Date();
const seedNum = now.getFullYear() * 10000 + (now.getMonth() + 1) * 100 + now.getDate();
const item = LINES[Math.floor(mulberry32(seedNum * 101)() * LINES.length)];
const widget = new ListWidget();
widget.backgroundColor = new Color("#221a1e");
widget.setPadding(16, 16, 16, 16);
const title = widget.addText("🐱 猫の一言");
title.font = Font.boldSystemFont(13);
title.textColor = new Color("#f9a8d4");
widget.addSpacer(10);
const big = widget.addText(item[0]);
big.font = Font.boldSystemFont(22);
big.textColor = new Color("#ffffff");
widget.addSpacer(4);
const face = widget.addText(item[1]);
face.font = Font.systemFont(18);
face.textColor = new Color("#f9c6dd");
Script.setWidget(widget);
""".trimIndent(),
        "🗓 今月だけ" to """
// プリセット: 今月だけ（ネット不要）
// ○月を大きく。あと何日で月が終わるかも
const now = new Date();
const lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 0).getDate();
const left = lastDay - now.getDate();
const widget = new ListWidget();
widget.backgroundColor = new Color("#12202b");
widget.setPadding(16, 16, 16, 16);
const big = widget.addText((now.getMonth() + 1) + "月");
big.font = Font.boldSystemFont(52);
big.textColor = new Color("#ffffff");
const sub = widget.addText(now.getFullYear() + "年 ・ あと " + left + " 日で月が変わります");
sub.font = Font.systemFont(13);
sub.textColor = new Color("#7cc7f0");
widget.addSpacer(8);
const row = widget.addStack();
row.layoutHorizontally();
row.addProgress(now.getDate() / lastDay, "#38bdf8", "#1d3242");
Script.setWidget(widget);
""".trimIndent(),
        "⏳ 今日の残り" to """
// プリセット: 今日の残り（ネット不要）
// 今日があと何%残っているか、それだけを大きく
const now = new Date();
const elapsed = now.getHours() * 3600 + now.getMinutes() * 60 + now.getSeconds();
const leftPct = Math.max(0, 100 - elapsed / 864);
const leftH = Math.floor((86400 - elapsed) / 3600);
const leftM = Math.floor(((86400 - elapsed) % 3600) / 60);
const widget = new ListWidget();
widget.backgroundColor = new Color("#131022");
widget.setPadding(16, 16, 16, 16);
const title = widget.addText("⏳ 今日の残り");
title.font = Font.boldSystemFont(13);
title.textColor = new Color("#ce9be8");
widget.addSpacer(6);
const big = widget.addText(leftPct.toFixed(0) + "%");
big.font = Font.boldSystemFont(48);
big.textColor = new Color("#ffffff");
const sub = widget.addText("あと " + leftH + " 時間 " + leftM + " 分で明日");
sub.font = Font.systemFont(13);
sub.textColor = new Color("#b8a8d9");
widget.addSpacer(8);
const row = widget.addStack();
row.layoutHorizontally();
row.addProgress(leftPct / 100, "#a78bfa", "#2a2140");
Script.setWidget(widget);
""".trimIndent(),
        "🎨 今日の色" to """
// プリセット: 今日の色（ネット不要）
// 日替わりの1色を大きく見せる。色コードつき
function mulberry32(a) {
  return function () {
    a |= 0; a = a + 0x6D2B79F5 | 0;
    let x = Math.imul(a ^ a >>> 15, 1 | a);
    x = x + Math.imul(x ^ x >>> 7, 61 | x) ^ x;
    return ((x ^ x >>> 14) >>> 0) / 4294967296;
  };
}
const COLORS = [
  ["珊瑚色", "#f88379"], ["若竹色", "#68be8d"], ["群青", "#36558f"], ["山吹色", "#f8b500"],
  ["藤色", "#a59aca"], ["浅葱色", "#00a3af"], ["茜色", "#eb471f"], ["抹茶色", "#c5c56a"],
  ["紺色", "#223a70"], ["桜色", "#fef4f4"], ["琥珀色", "#bf783a"], ["空色", "#a0d8ef"],
  ["撫子色", "#eebbcb"], ["萌葱色", "#006e54"], ["銀鼠", "#afafb0"], ["朱色", "#eb5505"],
  ["勿忘草色", "#89c3eb"], ["蘇芳色", "#9e3d3f"], ["白群", "#83ccd2"], ["柿色", "#ed6d3d"],
];
const now = new Date();
const seedNum = now.getFullYear() * 10000 + (now.getMonth() + 1) * 100 + now.getDate();
const item = COLORS[Math.floor(mulberry32(seedNum * 103)() * COLORS.length)];
const widget = new ListWidget();
widget.backgroundColor = new Color("#17171c");
widget.setPadding(14, 14, 14, 14);
const title = widget.addText("🎨 今日の色");
title.font = Font.boldSystemFont(13);
title.textColor = new Color("#ffffff");
widget.addSpacer(8);
const cv = widget.addCanvas(232, 110);
cv.roundRect(0, 0, 232, 110, 14, item[1]);
widget.addSpacer(8);
const name = widget.addText(item[0] + "　" + item[1]);
name.font = Font.boldSystemFont(16);
name.textColor = new Color("#ffffff");
Script.setWidget(widget);
""".trimIndent(),
        "👋 挨拶" to """
// プリセット: 挨拶（ネット不要）
// 時間帯で変わる挨拶と、いまの時刻だけ。いちばんシンプルなやつ
const now = new Date();
const h = now.getHours();
let g, color;
if (h < 5) { g = "おやすみなさい"; color = "#a5b8f0"; }
else if (h < 11) { g = "おはよう"; color = "#facc15"; }
else if (h < 17) { g = "こんにちは"; color = "#7cc7f0"; }
else if (h < 19) { g = "おつかれさま"; color = "#ffab7a"; }
else { g = "こんばんは"; color = "#ce9be8"; }
const pad = function (n) { return String(n).padStart(2, "0"); };
const widget = new ListWidget();
widget.backgroundColor = new Color("#14161c");
widget.setPadding(16, 16, 16, 16);
const big = widget.addText(g);
big.font = Font.boldSystemFont(34);
big.textColor = new Color(color);
const time = widget.addText(pad(h) + ":" + pad(now.getMinutes()));
time.font = Font.systemFont(15);
time.textColor = new Color("#9aa3b5");
Script.setWidget(widget);
""".trimIndent(),
        "🔭 万華鏡" to """
// 🔭 万華鏡（日替わり）
function mulberry32(a){return function(){a|=0;a=a+0x6D2B79F5|0;var t=Math.imul(a^a>>>15,1|a);t=t+Math.imul(t^t>>>7,61|t)^t;return((t^t>>>14)>>>0)/4294967296;};}
const now=new Date();const seed=now.getFullYear()*10000+(now.getMonth()+1)*100+now.getDate();
const rnd=mulberry32(seed);
const widget=new ListWidget();widget.setPadding(10,12,10,12);
const t=widget.addText("🔭 万華鏡");t.font=Font.boldSystemFont(13);t.textColor=new Color("#6a1b9a");
const cv=widget.addCanvas(300,190);
const cols=["#c2185b","#e65100","#6a1b9a","#0277bd","#f2a541","#43aa8b"];
const cx=150,cy=95;
for(let k=0;k<8;k++){
  const a0=k*Math.PI/4;
  for(let i=0;i<7;i++){
    const r=14+rnd()*74,th=a0+rnd()*0.6,s=4+rnd()*13;
    const x=cx+Math.cos(th)*r,y=cy+Math.sin(th)*r;
    const x2=cx+Math.cos(2*a0+Math.PI/4-th)*r,y2=cy+Math.sin(2*a0+Math.PI/4-th)*r;
    const c=cols[Math.floor(rnd()*cols.length)];
    cv.circle(x,y,s,c);cv.circle(x2,y2,s,c);
  }
}
cv.ring(cx,cy,88,2,"#00000022",0,360);
const s=widget.addText("今日の万華鏡。明日は別の模様。");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🌧 雨の窓" to """
// 🌧 雨の窓（日替わり）
function mulberry32(a){return function(){a|=0;a=a+0x6D2B79F5|0;var t=Math.imul(a^a>>>15,1|a);t=t+Math.imul(t^t>>>7,61|t)^t;return((t^t>>>14)>>>0)/4294967296;};}
const now=new Date();const seed=now.getFullYear()*10000+(now.getMonth()+1)*100+now.getDate();
const rnd=mulberry32(seed+31);
const widget=new ListWidget();widget.setPadding(10,12,10,12);
const t=widget.addText("🌧 雨の窓");t.font=Font.boldSystemFont(13);t.textColor=new Color("#0277bd");
const cv=widget.addCanvas(300,190);cv.background("#20303c");
for(let i=0;i<46;i++){const x=rnd()*300,y=rnd()*190,l=8+rnd()*22;cv.line(x,y,x-3,y+l,"#9fd4f2",1.4);}
for(let i=0;i<26;i++){cv.circle(rnd()*300,rnd()*190,1+rnd()*3.4,"#cfeafb");}
const s=widget.addText("窓の雨は日替わり。晴れの日も、これはこれで。");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🗻 等高線" to """
// 🗻 等高線（日替わり）
function mulberry32(a){return function(){a|=0;a=a+0x6D2B79F5|0;var t=Math.imul(a^a>>>15,1|a);t=t+Math.imul(t^t>>>7,61|t)^t;return((t^t>>>14)>>>0)/4294967296;};}
const now=new Date();const seed=now.getFullYear()*10000+(now.getMonth()+1)*100+now.getDate();
const rnd=mulberry32(seed+77);
const widget=new ListWidget();widget.setPadding(10,12,10,12);
const t=widget.addText("🗻 等高線");t.font=Font.boldSystemFont(13);t.textColor=new Color("#2e7d32");
const cv=widget.addCanvas(300,190);
const cx=90+rnd()*120,cy=60+rnd()*70;
for(let ringI=9;ringI>=1;ringI--){
  const base=ringI*13,pts=[];
  for(let i=0;i<28;i++){const th=i/28*Math.PI*2;const wob=1+0.16*Math.sin(th*3+ringI)+0.1*Math.sin(th*5+ringI*2);pts.push([cx+Math.cos(th)*base*wob,cy+Math.sin(th)*base*0.72*wob]);}
  for(let i=0;i<28;i++){const a=pts[i],b=pts[(i+1)%28];cv.line(a[0],a[1],b[0],b[1],ringI%3==0?"#2e7d32":"#9ccc9c",1.1);}
}
cv.circle(cx,cy,3,"#c2185b");
const s=widget.addText("架空の山の等高線。てっぺんの赤が山頂。");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🫧 泡のゆらぎ" to """
// 🫧 泡のゆらぎ（日替わり）
function mulberry32(a){return function(){a|=0;a=a+0x6D2B79F5|0;var t=Math.imul(a^a>>>15,1|a);t=t+Math.imul(t^t>>>7,61|t)^t;return((t^t>>>14)>>>0)/4294967296;};}
const now=new Date();const seed=now.getFullYear()*10000+(now.getMonth()+1)*100+now.getDate();
const rnd=mulberry32(seed+55);
const widget=new ListWidget();widget.setPadding(10,12,10,12);
const t=widget.addText("🫧 泡のゆらぎ");t.font=Font.boldSystemFont(13);t.textColor=new Color("#0277bd");
const cv=widget.addCanvas(300,190);cv.background("#0f2f3f");
const cols=["#7cc7f0","#bfe6fb","#f78fb8","#ffffff"];
for(let i=0;i<34;i++){const x=rnd()*300,y=rnd()*190,r=4+rnd()*22;cv.ring(x,y,r,1.6,0,360,cols[Math.floor(rnd()*cols.length)]);cv.circle(x-r*0.32,y-r*0.32,Math.max(1.4,r*0.16),"#ffffff");}
const s=widget.addText("深海の泡。今日の配置は今日だけ。");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🧱 日替わりタイル" to """
// 🧱 日替わりタイル（日替わり）
function mulberry32(a){return function(){a|=0;a=a+0x6D2B79F5|0;var t=Math.imul(a^a>>>15,1|a);t=t+Math.imul(t^t>>>7,61|t)^t;return((t^t>>>14)>>>0)/4294967296;};}
const now=new Date();const seed=now.getFullYear()*10000+(now.getMonth()+1)*100+now.getDate();
const rnd=mulberry32(seed+91);
const widget=new ListWidget();widget.setPadding(10,12,10,12);
const t=widget.addText("🧱 日替わりタイル");t.font=Font.boldSystemFont(13);t.textColor=new Color("#e65100");
const cv=widget.addCanvas(300,180);
const cols=["#c2185b","#e65100","#6a1b9a","#0277bd"];
const cell=36;
for(let gy=0;gy<5;gy++)for(let gx=0;gx<8;gx++){
  const x=gx*cell+6,y=gy*cell+2,c=cols[Math.floor(rnd()*cols.length)],kind=Math.floor(rnd()*3);
  if(kind==0)cv.circle(x+cell/2,y+cell/2,13,c);
  else if(kind==1)cv.rect(x+6,y+6,24,24,c);
  else cv.ring(x+cell/2,y+cell/2,12,5,0,360,c);
}
const s=widget.addText("タイルの並びは日替わり。");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "💊 薬の時間" to """
// 💊 薬の時間（予定は下の TIMES を書き換え）
const TIMES=["8:00","20:00"]; // 飲む時刻
const now=new Date();const nowMin=now.getHours()*60+now.getMinutes();
let next=null,done=0;
for(const ts of TIMES){const pp=ts.split(":");const m=parseInt(pp[0],10)*60+parseInt(pp[1],10);if(m<=nowMin)done++;if(next===null&&m>nowMin)next=ts;}
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("💊 薬の時間");t.font=Font.boldSystemFont(14);t.textColor=new Color("#0277bd");
const b=widget.addText(next?("つぎは "+next):"今日の分はおわり");b.font=Font.boldSystemFont(24);
const s=widget.addText("今日 "+done+" / "+TIMES.length+" 回ぶん過ぎました。時刻はスクリプト内の TIMES で変更できます");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🪴 水やりリズム" to """
// 🪴 水やりリズム（最終水やり日は LAST を書き換え）
const LAST="2026-11-20"; // 最後に水をあげた日
const EVERY=4;           // 何日おきか
const now=new Date();
const last=new Date(LAST+"T00:00:00");
const days=Math.floor((now-last)/86400000);
const rest=EVERY-(days%EVERY);
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🪴 水やりリズム");t.font=Font.boldSystemFont(14);t.textColor=new Color("#2e7d32");
const b=widget.addText(days%EVERY===0?"今日が水やりの日！":("つぎの水やりまで "+rest+" 日"));b.font=Font.boldSystemFont(19);
widget.addProgress(Math.min(1,(days%EVERY)/EVERY),"#43aa8b","#00000018");
const s=widget.addText("前回から "+days+" 日。水をあげたら LAST を今日に書き換えてね");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🧊 冷蔵庫メモ" to """
// 🧊 冷蔵庫メモ（中身は ITEMS を書き換え）
const ITEMS=["たまご 6個","牛乳 1本","豆腐 1丁","納豆 2パック","ヨーグルト"];
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🧊 冷蔵庫メモ（"+ITEMS.length+"品）");t.font=Font.boldSystemFont(14);t.textColor=new Color("#0277bd");
for(const it of ITEMS){const r=widget.addText("・"+it);r.font=Font.systemFont(14);}
const s=widget.addText("中身はスクリプト内の ITEMS で書き換えられます");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "😴 睡眠の目安" to """
// 😴 睡眠の目安（ふだんの時刻は下を書き換え）
const WAKE=7;    // 起床時刻
const SLEEP=24;  // 就寝時刻（24=0時、25=1時）
const GOAL=7;    // 目標の睡眠時間
const slept=(WAKE+24)-SLEEP;
const debt=Math.max(0,GOAL-slept);
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("😴 睡眠の目安");t.font=Font.boldSystemFont(14);t.textColor=new Color("#6a1b9a");
const b=widget.addText("睡眠 "+slept+" 時間 / 目標 "+GOAL+" 時間");b.font=Font.boldSystemFont(19);
widget.addProgress(Math.min(1,slept/GOAL),"#6a1b9a","#00000018");
const s=widget.addText((debt>0?("あと "+debt+" 時間、長く眠れると理想的。"):"目標クリア。いいリズムです。")+"（時刻はスクリプト内で変更できます）");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "👀 目の休憩" to """
// 👀 目の休憩（20-20-20 ルール）
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("👀 目の休憩 20-20-20");t.font=Font.boldSystemFont(14);t.textColor=new Color("#0277bd");
const steps=["20分 画面を見たら","20秒 休んで","20フィート（約6m）先を見る"];
for(const st of steps){const r=widget.addText("・"+st);r.font=Font.systemFont(14);}
const s=widget.addText("タップで開くたびに思い出す、小さな合図に。");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🥠 フォーチュンクッキー" to """
// 🥠 フォーチュンクッキー（日替わり）
const LIST=["今日の小さな親切が、来週の大きな運になる。","迷ったら、温かい飲み物を先に。","片づけは1か所だけでいい。運はそこから。","返信は短くていい。気持ちは伝わる。","新しい道は、帰り道で見つかる。","今日の我慢は明日の自分への貯金。","笑った回数を数える日。","雨の日は、いい音楽の日。"];
const now=new Date();const idx=(now.getFullYear()*372+(now.getMonth()+1)*31+now.getDate())%LIST.length;
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🥠 フォーチュンクッキー");t.font=Font.boldSystemFont(14);t.textColor=new Color("#e65100");
const b=widget.addText(LIST[idx]);b.font=Font.systemFont(15);
const s=widget.addText("今日のクッキーはこれ。明日また開けてね");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🍀 今日のラッキー" to """
// 🍀 今日のラッキー（日替わり）
function mulberry32(a){return function(){a|=0;a=a+0x6D2B79F5|0;var t=Math.imul(a^a>>>15,1|a);t=t+Math.imul(t^t>>>7,61|t)^t;return((t^t>>>14)>>>0)/4294967296;};}
const now=new Date();const seed=now.getFullYear()*10000+(now.getMonth()+1)*100+now.getDate();
const rnd=mulberry32(seed+7);
const ITEMS=["赤いペン","駅の階段","温かいスープ","古い写真","新しい靴ひも","窓際の席","白いハンカチ","朝の空"];
const num=1+Math.floor(rnd()*99);
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🍀 今日のラッキー");t.font=Font.boldSystemFont(14);t.textColor=new Color("#2e7d32");
const b=widget.addText("ナンバー "+num);b.font=Font.boldSystemFont(26);
const s=widget.addText("アイテムは「"+ITEMS[Math.floor(rnd()*ITEMS.length)]+"」。なんとなく味方です");s.font=Font.systemFont(11);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🥚 モンスターの卵" to """
// 🥚 モンスターの卵（孵化まであと少し）
const START="2026-11-25"; // 卵を置いた日
const HATCH=14;           // 孵化までの日数
const now=new Date();
const start=new Date(START+"T00:00:00");
const days=Math.floor((now-start)/86400000);
const left=Math.max(0,HATCH-days);
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🥚 モンスターの卵");t.font=Font.boldSystemFont(14);t.textColor=new Color("#6a1b9a");
const b=widget.addText(left>0?("孵化まで あと "+left+" 日"):("🐣 かえりました！"));b.font=Font.boldSystemFont(21);
widget.addProgress(Math.min(1,days/HATCH),"#6a1b9a","#00000018");
const s=widget.addText("置いてから "+days+" 日目。何が生まれるかはお楽しみ");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🌊 潮の目安" to """
// 🌊 潮の目安（月齢からの計算・目安です）
const now=new Date();
const known=Date.UTC(2000,0,6,18,14)/86400000; // 新月の基準
const age=((now.getTime()/86400000-known)%29.53059+29.53059)%29.53059;
const h=now.getHours()+now.getMinutes()/60;
const level=0.5+0.5*Math.sin((h/12.42)*Math.PI*2 - age*0.4); // ざっくりした潮位もどき
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🌊 潮の目安");t.font=Font.boldSystemFont(14);t.textColor=new Color("#0277bd");
const b=widget.addText(level>0.66?"満ち潮ぎみ":(level<0.33?"引き潮ぎみ":"中くらい"));b.font=Font.boldSystemFont(22);
widget.addProgress(Math.round(level*100)/100,"#0277bd","#00000018");
const s=widget.addText("月齢 "+age.toFixed(1)+" 日からの計算です。正確な潮位は公式の潮汐表で");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🚨 今日の地震" to """
// 🚨 今日の地震の回数（P2P地震情報・実データ）
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🚨 今日の地震");t.font=Font.boldSystemFont(14);t.textColor=new Color("#c2185b");
try{
  const res=await fetch("https://api.p2pquake.net/v2/jma/quake?limit=60");
  const data=await res.json();
  const now=new Date();
  const todayStr=now.getFullYear()+"/"+("0"+(now.getMonth()+1)).slice(-2)+"/"+("0"+now.getDate()).slice(-2);
  const scaleName=function(s){return {10:"1",20:"2",30:"3",40:"4",45:"5弱",50:"5強",55:"6弱",60:"6強",70:"7"}[s]||"-";};
  let count=0,maxS=0,latest="";
  for(const q of data){
    const eq=q.earthquake;if(!eq||!eq.time)continue;
    if(eq.time.indexOf(todayStr)===0){count++;if(eq.maxScale>maxS){maxS=eq.maxScale;}if(!latest)latest=eq.hypocenter.name;}
  }
  const b=widget.addText(count+" 回");b.font=Font.boldSystemFont(30);
  const s=widget.addText(count>0?("最大震度 "+scaleName(maxS)+"／直近 "+latest):"今日はまだ観測がありません");s.font=Font.systemFont(11);s.textColor=new Color("#888888");
}catch(e){
  const b=widget.addText("取得できませんでした");b.font=Font.systemFont(14);
}
Script.setWidget(widget);
""".trimIndent(),
        "📅 令和と干支" to """
// 📅 令和と干支（今年のまとめ）
const now=new Date();const y=now.getFullYear();
const reiwa=y-2018;
const eto=["申","酉","戌","亥","子","丑","寅","卯","辰","巳","午","未"][(y%12+12)%12];
const start=new Date(y,0,1);const doy=Math.floor((now-start)/86400000)+1;
const daysInYear=Math.round((new Date(y+1,0,1)-start)/86400000);
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("📅 令和と干支");t.font=Font.boldSystemFont(14);t.textColor=new Color("#6a1b9a");
const b=widget.addText("令和"+reiwa+"年・"+eto+"年");b.font=Font.boldSystemFont(24);
const s=widget.addText("今年 "+doy+" 日目 / 全 "+daysInYear+" 日");s.font=Font.systemFont(11);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "⏱ 今日の経過" to """
// ⏱ 今日の経過（秒まで数える）
const now=new Date();
const sec=now.getHours()*3600+now.getMinutes()*60+now.getSeconds();
const total=86400;
function fmt(n){return String(n).replace(/\B(?=(\d{3})+(?!\d))/g,",");}
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("⏱ 今日の経過");t.font=Font.boldSystemFont(14);t.textColor=new Color("#0277bd");
const b=widget.addText(fmt(sec)+" 秒");b.font=Font.boldSystemFont(28);
widget.addProgress(Math.round(sec/total*1000)/1000,"#0277bd","#00000018");
const s=widget.addText("今日はあと "+fmt(total-sec)+" 秒。表示した時点の数です");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "📖 今日のことわざ" to """
// 📖 今日のことわざ（日替わり）
const LIST=[["石の上にも三年","続ける力がいちばん強い。"],["急がば回れ","近道より、確実な道。"],["笑う門には福来たる","笑顔の家には幸運が。"],["雨降って地固まる","揉めた後は、かえって固まる。"],["七転び八起き","倒れても起きればいい。"],["塵も積もれば山となる","小さな積み重ねは大きい。"],["果報は寝て待て","焦らず、運は待つもの。"],["好きこそ物の上手なれ","好きは最強の練習。"]];
const now=new Date();const idx=(now.getFullYear()*372+(now.getMonth()+1)*31+now.getDate())%LIST.length;
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("📖 今日のことわざ");t.font=Font.boldSystemFont(14);t.textColor=new Color("#6a1b9a");
const b=widget.addText(LIST[idx][0]);b.font=Font.boldSystemFont(20);
const s=widget.addText(LIST[idx][1]);s.font=Font.systemFont(11);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🧮 計算ドリル" to """
// 🧮 計算ドリル（日替わり3問・答えは小さく）
function mulberry32(a){return function(){a|=0;a=a+0x6D2B79F5|0;var t=Math.imul(a^a>>>15,1|a);t=t+Math.imul(t^t>>>7,61|t)^t;return((t^t>>>14)>>>0)/4294967296;};}
const now=new Date();const seed=now.getFullYear()*10000+(now.getMonth()+1)*100+now.getDate();
const rnd=mulberry32(seed+3);
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🧮 今日の計算ドリル");t.font=Font.boldSystemFont(14);t.textColor=new Color("#e65100");
const a1=12+Math.floor(rnd()*80),b1=3+Math.floor(rnd()*40);
const a2=30+Math.floor(rnd()*60),b2=3+Math.floor(rnd()*25);
const a3=2+Math.floor(rnd()*8),b3=2+Math.floor(rnd()*9);
const qs=[[a1+" + "+b1+" = ?",a1+b1],[a2+" − "+b2+" = ?",a2-b2],[a3+" × "+b3+" = ?",a3*b3]];
for(const q of qs){const r=widget.addText(q[0]);r.font=Font.boldSystemFont(17);}
const s=widget.addText("答え： "+qs.map(function(q){return q[1];}).join(" ／ "));s.font=Font.systemFont(10);s.textColor=new Color("#bbbbbb");
Script.setWidget(widget);
""".trimIndent(),
        "🗾 今日の都道府県" to """
// 🗾 今日の都道府県（日替わり）
const LIST=[["北海道","札幌",""],["青森県","青森",""],["岩手県","盛岡",""],["宮城県","仙台",""],["秋田県","秋田",""],["山形県","山形",""],["福島県","福島",""],["茨城県","水戸",""],["栃木県","宇都宮",""],["群馬県","前橋",""],["埼玉県","さいたま",""],["千葉県","千葉",""],["東京都","新宿（東京）",""],["神奈川県","横浜",""],["新潟県","新潟",""],["富山県","富山",""],["石川県","金沢",""],["福井県","福井",""],["山梨県","甲府",""],["長野県","長野",""],["岐阜県","岐阜",""],["静岡県","静岡",""],["愛知県","名古屋",""],["三重県","津",""],["滋賀県","大津",""],["京都府","京都",""],["大阪府","大阪",""],["兵庫県","神戸",""],["奈良県","奈良",""],["和歌山県","和歌山",""],["鳥取県","鳥取",""],["島根県","松江",""],["岡山県","岡山",""],["広島県","広島",""],["山口県","山口",""],["徳島県","徳島",""],["香川県","高松",""],["愛媛県","松山",""],["高知県","高知",""],["福岡県","福岡",""],["佐賀県","佐賀",""],["長崎県","長崎",""],["熊本県","熊本",""],["大分県","大分",""],["宮崎県","宮崎",""],["鹿児島県","鹿児島",""],["沖縄県","那覇",""]];
const now=new Date();const idx=(now.getFullYear()*372+(now.getMonth()+1)*31+now.getDate())%47;
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🗾 今日の都道府県");t.font=Font.boldSystemFont(14);t.textColor=new Color("#0277bd");
const b=widget.addText(LIST[idx][0]);b.font=Font.boldSystemFont(26);
const s=widget.addText("県庁所在地は "+LIST[idx][1]+"。47分の1、今日はここ");s.font=Font.systemFont(11);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🌠 今夜の星座" to """
// 🌠 今夜の星座（季節のおすすめ）
const now=new Date();const m=now.getMonth()+1;
const DATA={12:["オリオン座","冬の大三角が主役。ベテルギウスを探して。"],1:["おうし座","プレアデス（すばる）が見ごろ。"],2:["ふたご座","カストルとポルックスの兄弟星。"],3:["しし座","春の始まりの合図。"],4:["おとめ座","スピカの一等星が目印。"],5:["うしかい座","オレンジ色のアルクトゥルス。"],6:["さそり座","赤いアンタレスとS字の並び。"],7:["夏の大三角","ベガ・アルタイル・デネブ。七夕の空。"],8:["夏の大三角","ペルセウス座流星群の季節でも。"],9:["ペガスス座","秋の四辺形が目印。"],10:["カシオペヤ座","Wの形。北の空の道しるべ。"],11:["アンドロメダ座","銀河が見える（暗い空で）。"]};
const d=DATA[m];
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🌠 今夜の星座");t.font=Font.boldSystemFont(14);t.textColor=new Color("#6a1b9a");
const b=widget.addText(d[0]);b.font=Font.boldSystemFont(22);
const s=widget.addText(d[1]);s.font=Font.systemFont(11);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "☕ 豆メモ" to """
// ☕ 豆メモ（豆と挽き方は BEANS を書き換え）
const BEANS=[["エチオピア","中煎り・浅め","柑橘の香り"],["ブラジル","中深煎り","ナッツとチョコ"],["コロンビア","中煎り","まろやか"]];
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("☕ 豆メモ");t.font=Font.boldSystemFont(14);t.textColor=new Color("#6d4c41");
for(const bn of BEANS){const r=widget.addText("・"+bn[0]+"（"+bn[1]+"）");r.font=Font.systemFont(13);const s2=widget.addText("　"+bn[2]);s2.font=Font.systemFont(10);s2.textColor=new Color("#888888");}
const s=widget.addText("豆の情報はスクリプト内の BEANS で書き換えられます");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🌙 夢メモ" to """
// 🌙 夢メモ（朝のひとことテンプレ）
const now=new Date();
const wd=["日","月","火","水","木","金","土"][now.getDay()];
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🌙 夢メモ");t.font=Font.boldSystemFont(14);t.textColor=new Color("#6a1b9a");
const b=widget.addText((now.getMonth()+1)+"/"+now.getDate()+"（"+wd+"）の夢");b.font=Font.boldSystemFont(18);
const lines=["・誰が出てきた？","・場所はどこ？","・気分はどうだった？"];
for(const ln of lines){const r=widget.addText(ln);r.font=Font.systemFont(13);r.textColor=new Color("#888888");}
const s=widget.addText("起きたら30秒でメモ。消える前に。");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🙏 今日の感謝" to """
// 🙏 今日の感謝（日替わりテーマ）
const LIST=["温かい飲み物を用意してくれた誰か","昨日よく眠れたこと","届いた小さな連絡","体が動いてくれること","おいしかった一食","誰かの何気ない一言","雨をしのげた屋根","今日の空の色"];
const now=new Date();const idx=(now.getFullYear()*372+(now.getMonth()+1)*31+now.getDate())%LIST.length;
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🙏 今日の感謝");t.font=Font.boldSystemFont(14);t.textColor=new Color("#e65100");
const b=widget.addText(LIST[idx]);b.font=Font.systemFont(16);
const s=widget.addText("ひとつ思い浮かべるだけで十分、の日課。");s.font=Font.systemFont(10);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent(),
        "🎸 今日のコード進行" to """
// 🎸 今日のコード進行（日替わり）
const KEYS=["C","G","D","A","F"];
const PROGS=[["I – V – vi – IV","王道進行"],["vi – IV – I – V","切ない王道"],["I – vi – IV – V","50年代の香り"],["IV – I – V – vi","壮大なサビ向き"],["I – IV – V – IV","シンプルで強い"]];
const now=new Date();const idx=(now.getFullYear()*372+(now.getMonth()+1)*31+now.getDate());
const widget=new ListWidget();widget.setPadding(12,14,12,14);
const t=widget.addText("🎸 今日のコード進行");t.font=Font.boldSystemFont(14);t.textColor=new Color("#c2185b");
const b=widget.addText(PROGS[idx%PROGS.length][0]);b.font=Font.boldSystemFont(20);
const s=widget.addText("キー "+KEYS[idx%KEYS.length]+" ／ "+PROGS[idx%PROGS.length][1]);s.font=Font.systemFont(11);s.textColor=new Color("#888888");
Script.setWidget(widget);
""".trimIndent()
    )
}
