package app.scriptlet

import android.appwidget.AppWidgetManager
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.scriptlet.data.ScriptItem
import app.scriptlet.data.ScriptStore
import app.scriptlet.engine.RunResult
import app.scriptlet.engine.SampleScripts
import app.scriptlet.engine.ScriptEngine
import app.scriptlet.ui.Tokens
import app.scriptlet.ui.WidgetPreview
import app.scriptlet.widget.ScriptletWidgetReceiver
import app.scriptlet.widget.WidgetConfigActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val store by lazy { app.scriptlet.data.StoreProvider.get(this) }
    private val engine by lazy { ScriptEngine(store) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            app.scriptlet.ui.ScriptletTheme {
                androidx.compose.material3.Surface(modifier = Modifier.fillMaxSize()) {
                    ScriptletApp(store, engine)
                }
            }
        }
    }
}

/** One-line summary for pickers: stored description, else sample description by name, else the first real code line. */
/** Splits a leading emoji cluster off a script name for the list tile: (emoji, rest). */
fun splitLeadingEmoji(name: String): Pair<String?, String> {
    var idx = 0
    var found = false
    while (idx < name.length) {
        val cp = name.codePointAt(idx)
        val pictographic = cp in 0x1F000..0x1FAFF || cp in 0x2600..0x27BF ||
            cp in 0x2300..0x23FF || cp in 0x2B00..0x2BFF
        val glue = cp == 0xFE0F || cp == 0x200D || cp == 0x20E3
        if (pictographic) {
            found = true
            idx += Character.charCount(cp)
        } else if (glue && found) {
            idx += Character.charCount(cp)
        } else {
            break
        }
    }
    if (!found) return null to name
    val rest = name.substring(idx).trim()
    return name.substring(0, idx) to rest.ifEmpty { name }
}

/**
 * Face tile for a script: its leading emoji (or first letter) on a soft
 * brand tint, rotated through the 4 brand roles by a stable seed hash.
 * Shared by the list cards and the new-script chooser so a script looks
 * like the same friendly tool in both places.
 */
@Composable
fun EmojiTile(name: String, seed: String, size: Dp = 48.dp, emojiSize: Int = 24) {
    val (emoji, _) = splitLeadingEmoji(name)
    val tileTints = listOf(Tokens.Pink, Tokens.Orange, Tokens.Purple, Tokens.Sky)
    val tint = tileTints[(seed.hashCode() and Int.MAX_VALUE) % tileTints.size]
    Box(
        modifier = Modifier.size(size)
            .background(tint.copy(alpha = 0.16f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        if (emoji != null) {
            Text(emoji, fontSize = emojiSize.sp)
        } else {
            Text(
                name.take(1),
                style = MaterialTheme.typography.titleLarge,
                color = tint,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

fun scriptSummary(item: ScriptItem): String =
    item.desc.ifBlank {
        SampleScripts.descForName(item.name) ?: item.code.lineSequence()
            .map { it.trim() }
            .firstOrNull { it.isNotEmpty() && !it.startsWith("//") }
            ?.take(42)
            ?: ""
    }

/** Asks the launcher to pin a widget for [scriptId] (the config screen preselects it when the launcher passes the extra through). */
fun requestPinWidget(context: Context, scriptId: String): Boolean {
    val manager = AppWidgetManager.getInstance(context)
    if (!manager.isRequestPinAppWidgetSupported) return false
    val provider = ComponentName(context, ScriptletWidgetReceiver::class.java)
    val extras = Bundle().apply { putString(WidgetConfigActivity.EXTRA_SCRIPT_ID, scriptId) }
    return manager.requestPinAppWidget(provider, extras, null)
}

/**
 * Scripts changed (edited, renamed, duplicated, deleted): every placed
 * widget re-renders from the store, so push an update now instead of
 * leaving stale renderings on the home screen until the next periodic
 * update (up to 30 minutes later) or a manual tap.
 */
@android.annotation.SuppressLint("RestrictedApi") // AppWidgetId: same targeted suppression as WidgetConfigActivity (Glance 1.1.1 has no public id type)
fun refreshWidgetsAsync(context: Context) {
    val appContext = context.applicationContext
    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
        val manager = AppWidgetManager.getInstance(appContext)
        // All three size kinds: each placed widget is updated through the
        // implementation class that owns its provider.
        app.scriptlet.widget.WidgetKinds.receiverClasses.forEach { receiver ->
            val ids = manager.getAppWidgetIds(ComponentName(appContext, receiver))
            ids.forEach { id ->
                runCatching {
                    app.scriptlet.widget.WidgetKinds.widgetFor(appContext, id)
                        .update(appContext, androidx.glance.appwidget.AppWidgetId(id))
                }
            }
        }
    }
}

@Composable
private fun ScriptletApp(store: ScriptStore, engine: ScriptEngine) {
    val appContext = LocalContext.current
    val prefs = remember {
        appContext.getSharedPreferences("scriptlet_prefs", Context.MODE_PRIVATE)
    }
    // First launch (per onboarding version): show the onboarding before the
    // list. Completing or skipping records the version and never shows again.
    var onboardingDone by remember { mutableStateOf(prefs.getInt("onboarding_version", 0) >= 3) }
    var editingId by remember { mutableStateOf<String?>(null) }
    var createSeed by remember { mutableStateOf<Triple<String, String, String>?>(null) }
    var showHelp by remember { mutableStateOf(false) }
    // Manual replay from the help screen: same onboarding, but leaving it
    // returns to the list and the recorded version is left untouched.
    var replayOnboarding by remember { mutableStateOf(false) }

    when {
        !onboardingDone || replayOnboarding -> app.scriptlet.ui.OnboardingScreen(
            engine = engine,
            onFinish = { name, code, desc ->
                prefs.edit().putInt("onboarding_version", 3).apply()
                onboardingDone = true
                replayOnboarding = false
                createSeed = Triple(name, code, desc)
            },
            onSkip = {
                prefs.edit().putInt("onboarding_version", 3).apply()
                onboardingDone = true
                replayOnboarding = false
            },
        )
        showHelp -> HelpScreen(
            onBack = { showHelp = false },
            onReplayOnboarding = {
                showHelp = false
                replayOnboarding = true
            },
        )
        createSeed != null -> EditorScreen(
            store = store,
            engine = engine,
            initial = null,
            seedName = createSeed!!.first,
            seedCode = createSeed!!.second,
            seedDesc = createSeed!!.third,
            onBack = { createSeed = null },
        )
        editingId != null -> {
            val item = store.scripts.collectAsState().value.firstOrNull { it.id == editingId }
            // The script may have been deleted elsewhere: leave the editor from
            // an effect, never by writing state during composition.
            androidx.compose.runtime.LaunchedEffect(item) {
                if (item == null) editingId = null
            }
            if (item != null) {
                EditorScreen(
                    store = store,
                    engine = engine,
                    initial = item,
                    onBack = { editingId = null },
                )
            }
        }
        else -> ListScreen(
            store = store,
            onOpen = { editingId = it },
            onNew = { name, code, desc -> createSeed = Triple(name, code, desc) },
            onHelp = { showHelp = true },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListScreen(store: ScriptStore, onOpen: (String) -> Unit, onNew: (String, String, String) -> Unit, onHelp: () -> Unit) {
    val context = LocalContext.current
    val scripts by store.scripts.collectAsState()
    val fmt = remember { SimpleDateFormat("MM/dd HH:mm", Locale.JAPAN) }
    var showChooser by remember { mutableStateOf(false) }
    var pinNote by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scriptlet") },
                actions = {
                    IconButton(onClick = onHelp) {
                        Text(
                            "？",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            androidx.compose.material3.ExtendedFloatingActionButton(
                onClick = { showChooser = true },
                icon = { Text("＋", style = MaterialTheme.typography.titleLarge) },
                text = { Text("新規") },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            // Generous bottom padding so the last card can scroll fully
            // clear of the extended FAB (it used to stay half-hidden).
            contentPadding = PaddingValues(
                start = Tokens.ScreenPadding,
                end = Tokens.ScreenPadding,
                top = Tokens.ScreenPadding,
                bottom = 96.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing),
        ) {
            item {
                // Welcome band: the app's promise and the easiest first step,
                // so a first-time viewer thinks "I could do this too".
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(Tokens.CardRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                ) {
                    Column(modifier = Modifier.padding(Tokens.CardPadding)) {
                        Text(
                            "ホーム画面を、自分の道具に",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "スクリプトを選ぶと編集が始まります。まずはサンプルを「実行プレビュー」で試すのがおすすめ。",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        TextButton(onClick = { showChooser = true }) {
                            Text("サンプルから作る →", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            pinNote?.let { note ->
                item {
                    Text(note, color = MaterialTheme.colorScheme.primary)
                }
            }
            items(scripts, key = { it.id }) { item ->
                ScriptCard(
                    item = item,
                    updatedLabel = "更新 ${fmt.format(Date(item.updatedAt))}",
                    onOpen = { onOpen(item.id) },
                    onPin = {
                        pinNote = if (requestPinWidget(context, item.id)) {
                            "ホーム画面への追加を確認してください"
                        } else {
                            "この端末のランチャーではアプリからの追加に対応していません"
                        }
                    },
                    onDuplicate = { store.create(item.name + " のコピー", item.code, item.desc); refreshWidgetsAsync(context) },
                    onRename = { newName -> store.update(item.copy(name = newName)); refreshWidgetsAsync(context) },
                    onDelete = { store.delete(item.id); refreshWidgetsAsync(context) },
                )
            }
            if (scripts.isEmpty()) {
                item {
                    Text(
                        "スクリプトがありません。＋から作成できます。",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }

    if (showChooser) {
        // Wide custom dialog (the platform AlertDialog was too narrow to
        // read descriptions comfortably). Entries are mini-cards with the
        // same face tiles as the list, so picking feels like browsing a
        // shelf of ready-made tools — not reading a file menu.
        Dialog(
            onDismissRequest = { showChooser = false },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(0.94f).fillMaxHeight(0.88f),
                shape = RoundedCornerShape(20.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "新しいスクリプト",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "選ぶだけですぐ使えます。あとから中身も自由に変えられます。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(Tokens.ItemSpacing))
                    Column(
                        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(Tokens.SmallSpacing),
                    ) {
                        @Composable
                        fun sampleEntry(index: Int) {
                            val (sampleName, sampleCode) = SampleScripts.all[index]
                            val sampleDesc = SampleScripts.descs.getOrElse(index) { "" }
                            val (_, restName) = splitLeadingEmoji(sampleName)
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    showChooser = false
                                    onNew(sampleName, sampleCode, sampleDesc)
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    EmojiTile(
                                        name = sampleName,
                                        seed = SampleScripts.keys[index],
                                        size = 40.dp,
                                        emojiSize = 20,
                                    )
                                    Spacer(Modifier.size(Tokens.ItemSpacing))
                                    Column {
                                        Text(restName, style = MaterialTheme.typography.titleMedium)
                                        if (sampleDesc.isNotEmpty()) {
                                            Text(
                                                sampleDesc,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Text(
                            "おすすめ",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        )
                        SampleScripts.all.indices
                            .filter { SampleScripts.keys[it] in SampleScripts.featuredKeys }
                            .forEach { sampleEntry(it) }
                        Spacer(Modifier.height(Tokens.SmallSpacing))
                        Text(
                            "そのほかのサンプル",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        )
                        SampleScripts.all.indices
                            .filter { SampleScripts.keys[it] !in SampleScripts.featuredKeys }
                            .forEach { sampleEntry(it) }
                        Spacer(Modifier.height(Tokens.SmallSpacing))
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable {
                                showChooser = false
                                onNew("新しいスクリプト", "// ここにスクリプトを書く\nconst widget = new ListWidget();\n", "")
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                EmojiTile(name = "✍️ まっさら", seed = "blank", size = 40.dp, emojiSize = 20)
                                Spacer(Modifier.size(Tokens.ItemSpacing))
                                Column {
                                    Text("まっさらから書く", style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "空白のスクリプト。自分でコードを書きたい人向け",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(onClick = { showChooser = false }) { Text("キャンセル") }
                    }
                }
            }
        }
    }
}

/**
 * Help: how the app works, a mini writing guide, troubleshooting, and the
 * door back into the onboarding. Static content only — nothing here runs
 * or saves anything; the one filled button is the onboarding replay.
 */
/** Ready-made prompt for asking an external AI to write a Scriptlet
 *  script (user direction, 2026-11-15: keep it in Help, one tap to copy).
 *  The API summary must stay in step with Prelude.kt. */
private const val AI_PROMPT = """あなたは「Scriptlet」というAndroidアプリ用のJavaScriptを書くアシスタントです。Scriptletは、JavaScriptを書くとホーム画面ウィジェットになるアプリです。以下のルールとAPIだけを使って、動くコードを1つだけ、コードブロックで出力してください。

【ルール】
- 最初に const widget = new ListWidget(); と書き、最後に Script.setWidget(widget); で終わる
- fetch を使うときは await fetch(...) と書く（トップレベルで await が使えます）
- 使えるのは下記のAPIだけ。それ以外の関数やライブラリは使わない
- 通信は try/catch で囲み、失敗したときは代わりの文字を表示する

【API】
- widget.addText("文字") → Text。設定: font = Font.systemFont(14) / Font.boldSystemFont(14)、textColor = new Color("#333333")、align = "left" / "center" / "right"
- widget.addStack() → Stack。stack.layoutHorizontally() / stack.layoutVertically() で並び方、stack.spacing = 8 で間隔。中にも addText など同じ部品が入れられる
- widget.addSpacer(12) 余白 / widget.addDivider() 区切り線
- widget.addProgress(0.6) 進み具合バー（0〜1）
- widget.addImage("https://…") 画像
- widget.addCanvas(幅, 高さ) → Canvas。c.bg("#色")、c.rect(x, y, w, h, "#色")、c.circle(x, y, r, "#色")、c.line(x1, y1, x2, y2, "#色", 太さ)、c.text("文字", x, y, サイズ, "#色")、c.ring(x, y, r, 割合, 太さ, "#色")
- ヘルパー: PieChart(canvas, x, y, r, [値...], ["#色"...]) / BarChart(canvas, x, y, w, h, [値...], "#色")
- widget.backgroundColor = new Color("#ffffff") / widget.setPadding(12, 12, 12, 12)
- const res = await fetch("https://…"); const json = await res.json(); const text = await res.text();
- Storage.get("キー") / Storage.set("キー", "値") でスクリプトごとに保存できる（文字列のみ）
- Date、Math、JSON など通常の JavaScript が使える

【作りたいもの】
（ここに作りたいウィジェットを書いてください。例: 大阪の天気を大きく表示したい）"""

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HelpScreen(onBack: () -> Unit, onReplayOnboarding: () -> Unit) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    var promptCopied by remember { mutableStateOf(false) }
    val versionName = remember {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: ""
        } catch (e: Exception) {
            ""
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("使い方") },
                navigationIcon = { TextButton(onClick = onBack) { Text("← 一覧") } },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(Tokens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing),
        ) {
            // Visual help (user direction, 2026-11-15): icons and shapes
            // carry the meaning, text only supports. Section heads get an
            // emoji tile in a brand tint; flows are drawn as steps.
            @Composable
            fun sectionHead(emoji: String, text: String, ink: Color) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(38.dp).clip(RoundedCornerShape(11.dp))
                            .background(ink.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center,
                    ) { Text(emoji, fontSize = 19.sp) }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            @Composable
            fun body(text: String) {
                Text(text, style = MaterialTheme.typography.bodyMedium)
            }
            @Composable
            fun stepRow(num: Int, title: String, desc: String) {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(Tokens.CardRadius)) {
                    Row(
                        modifier = Modifier.padding(Tokens.CardPadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier.size(28.dp).clip(CircleShape).background(Tokens.Pink),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                num.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(
                                desc,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
            @Composable
            fun stepArrow() {
                Text(
                    "▼",
                    color = Tokens.Pink.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
            @Composable
            fun partCard(emoji: String, name: String, desc: String, ink: Color, modifier: Modifier) {
                Card(modifier = modifier, shape = RoundedCornerShape(Tokens.CardRadius)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Box(
                            modifier = Modifier.size(34.dp).clip(RoundedCornerShape(10.dp))
                                .background(ink.copy(alpha = 0.14f)),
                            contentAlignment = Alignment.Center,
                        ) { Text(emoji, fontSize = 17.sp) }
                        Spacer(Modifier.height(6.dp))
                        Text(name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(
                            desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            @Composable
            fun troubleRow(emoji: String, ink: Color, text: String) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier.size(34.dp).clip(RoundedCornerShape(10.dp))
                            .background(ink.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center,
                    ) { Text(emoji, fontSize = 17.sp) }
                    Spacer(Modifier.width(10.dp))
                    Text(text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                }
            }

            sectionHead("🧭", "基本の流れ（3ステップ）", Tokens.Pink)
            stepRow(1, "サンプルを選ぶ", "「＋ 新規」から。おすすめが近道です")
            stepArrow()
            stepRow(2, "実行して確かめる", "編集画面で「▶ 実行プレビュー」を押す")
            stepArrow()
            stepRow(3, "ホーム画面に置く", "「ホーム画面に追加」→ 内容を決めて「この内容で決定」")

            sectionHead("🔄", "ウィジェットが更新されるタイミング", Tokens.Sky)
            Row(horizontalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing)) {
                partCard("🕐", "自動更新", "最短30分ごと（Androidの仕組み）", Tokens.Sky, Modifier.weight(1f))
                partCard("👆", "タップ更新", "ウィジェットを押すと、その場で更新", Tokens.Pink, Modifier.weight(1f))
            }
            troubleRow("💡", Tokens.Sky, "時計などに表示される時刻は「更新された時点」のものです。")

            sectionHead("✏️", "スクリプトの書き方", Tokens.Purple)
            body("いちばん小さな形はこれです。")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Tokens.CardRadius),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            ) {
                Text(
                    "const widget = new ListWidget();\n" +
                        "const t = widget.addText(\"こんにちは\");\n" +
                        "t.font = Font.boldSystemFont(18);\n" +
                        "Script.setWidget(widget);",
                    modifier = Modifier.padding(Tokens.CardPadding),
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                )
            }
            body("使える部品はこの8つ。")
            Row(horizontalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing)) {
                partCard("🔤", "addText", "文字を出す", Tokens.Pink, Modifier.weight(1f))
                partCard("🧱", "addStack", "横・縦に並べる", Tokens.Orange, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing)) {
                partCard("↔️", "addSpacer", "余白をあける", Tokens.Purple, Modifier.weight(1f))
                partCard("➖", "addDivider", "区切り線", Tokens.Sky, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing)) {
                partCard("🖼️", "addImage", "画像を出す", Tokens.Orange, Modifier.weight(1f))
                partCard("📊", "addProgress", "進み具合バー", Tokens.Pink, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing)) {
                partCard("🎨", "addCanvas", "自由に描く", Tokens.Purple, Modifier.weight(1f))
                partCard("🔗", "addLink", "リンクを置く", Tokens.Sky, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing)) {
                partCard("🌐", "fetch", "ネットからデータを取る", Tokens.Sky, Modifier.weight(1f))
                partCard("💾", "Storage", "値を保存する（スクリプトごと）", Tokens.Orange, Modifier.weight(1f))
            }
            body("色は new Color(\"#c2185b\")、文字の大きさは Font.systemFont(14) / Font.boldSystemFont(14) です。")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Tokens.CardRadius),
                colors = CardDefaults.cardColors(containerColor = Tokens.Sky.copy(alpha = 0.10f)),
            ) {
                Row(modifier = Modifier.padding(Tokens.CardPadding), verticalAlignment = Alignment.Top) {
                    Text("💡", fontSize = 18.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "サンプルのコードを読むのが一番の近道です。気になるサンプルを開いて、数字や文字を1つ変えて実行してみてください。",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            sectionHead("🤖", "AIに書いてもらう", Tokens.Orange)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                listOf("① コピーする", "② AIに貼る", "③ コードを貼り戻す").forEachIndexed { i, label ->
                    if (i > 0) {
                        Text("→", color = Tokens.Orange, fontWeight = FontWeight.Bold)
                    }
                    Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }
            }
            body("ChatGPTやClaudeなどのAIに、下のプロンプトを貼って「作りたいもの」を書き足すと、Scriptletでそのまま動くコードを書いてもらえます。")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Tokens.CardRadius),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            ) {
                Text(
                    AI_PROMPT,
                    modifier = Modifier.padding(Tokens.CardPadding),
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                )
            }
            OutlinedButton(
                onClick = {
                    clipboard.setText(AnnotatedString(AI_PROMPT))
                    promptCopied = true
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text(if (promptCopied) "✓ コピーしました" else "📋 プロンプトをコピー") }

            sectionHead("🛠", "うまくいかないとき", Tokens.Orange)
            troubleRow("🧭", Tokens.Pink, "エラーは行番号つきで表示されます。その行を見直してみてください。")
            troubleRow("🔒", Tokens.Sky, "fetch は https のURLだけ使えます。通信が不安定なときは、もう一度実行すると通ることがあります。")
            troubleRow("⏱", Tokens.Purple, "実行が長く終わらないときは、20秒でいったん区切ってエラーを表示します。詰まった実行は裏側で自動的に切り替わるので、少し待ってからもう一度押してください。")
            troubleRow("🧩", Tokens.Orange, "ウィジェットが「未設定」と出るときは、そのウィジェットを削除して、アプリから追加し直してください。")

            sectionHead("🔁", "最初から体験し直す", Tokens.Pink)
            body("初回に出てきた案内（オンボーディング）を、もう一度見ることができます。")
            Button(
                onClick = onReplayOnboarding,
                modifier = Modifier.fillMaxWidth(),
            ) { Text("オンボーディングをもう一度見る") }

            Text(
                "Scriptlet" + if (versionName.isNotEmpty()) " v$versionName" else "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ScriptCard(
    item: ScriptItem,
    updatedLabel: String,
    onOpen: () -> Unit,
    onPin: () -> Unit,
    onDuplicate: () -> Unit,
    onRename: (String) -> Unit,
    onDelete: () -> Unit,
) {
    var menuOpen by remember { mutableStateOf(false) }
    var renameOpen by remember { mutableStateOf(false) }
    var deleteOpen by remember { mutableStateOf(false) }
    var renameText by remember { mutableStateOf(item.name) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onOpen() },
        shape = RoundedCornerShape(Tokens.CardRadius),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Face tile (shared part): the list reads as a shelf of friendly
            // tools, not grey file rows.
            val (_, restName) = splitLeadingEmoji(item.name)
            EmojiTile(name = item.name, seed = item.id)
            Spacer(Modifier.size(Tokens.ItemSpacing))
            Column(modifier = Modifier.weight(1f)) {
                Text(restName, style = MaterialTheme.typography.titleMedium)
                val summary = scriptSummary(item)
                if (summary.isNotEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    updatedLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = { menuOpen = true }) {
                Text("⋮", style = MaterialTheme.typography.titleLarge)
            }
            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                DropdownMenuItem(text = { Text("開く") }, onClick = { menuOpen = false; onOpen() })
                DropdownMenuItem(text = { Text("ホーム画面に追加") }, onClick = { menuOpen = false; onPin() })
                DropdownMenuItem(text = { Text("複製") }, onClick = { menuOpen = false; onDuplicate() })
                DropdownMenuItem(text = { Text("名前を変更") }, onClick = { menuOpen = false; renameText = item.name; renameOpen = true })
                DropdownMenuItem(text = { Text("削除") }, onClick = { menuOpen = false; deleteOpen = true })
            }
        }
    }

    if (renameOpen) {
        AlertDialog(
            onDismissRequest = { renameOpen = false },
            title = { Text("名前を変更") },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (renameText.isNotBlank()) onRename(renameText)
                    renameOpen = false
                }) { Text("変更") }
            },
            dismissButton = { TextButton(onClick = { renameOpen = false }) { Text("キャンセル") } },
        )
    }
    if (deleteOpen) {
        AlertDialog(
            onDismissRequest = { deleteOpen = false },
            title = { Text("削除しますか") },
            text = { Text("「${item.name}」を削除します。元に戻せません。") },
            confirmButton = {
                TextButton(onClick = { deleteOpen = false; onDelete() }) { Text("削除") }
            },
            dismissButton = { TextButton(onClick = { deleteOpen = false }) { Text("キャンセル") } },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorScreen(
    store: ScriptStore,
    engine: ScriptEngine,
    initial: ScriptItem?,
    seedName: String = "新しいスクリプト",
    seedCode: String = "// ここにスクリプトを書く\nconst widget = new ListWidget();\n",
    seedDesc: String = "",
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentId by remember { mutableStateOf(initial?.id) }
    var name by remember { mutableStateOf(initial?.name ?: seedName) }
    var code by remember { mutableStateOf(initial?.code ?: seedCode) }
    var running by remember { mutableStateOf(false) }
    var editorMenu by remember { mutableStateOf(false) }
    var pasteNote by remember { mutableStateOf<String?>(null) }
    var pinNote by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<RunResult?>(null) }
    var bigPreview by remember { mutableStateOf<RunResult.Success?>(null) }
    var pendingPaste by remember { mutableStateOf<String?>(null) }
    var deleteConfirm by remember { mutableStateOf(false) }

    // Dirty tracking compares against the last *saved* snapshot, not the item
    // as it was when the editor opened: otherwise every save leaves the editor
    // permanently "dirty" and each preview run rewrites the file (bumping the
    // updated-at timestamp and reshuffling the list) without any real change.
    var savedName by remember { mutableStateOf(initial?.name ?: seedName) }
    var savedCode by remember { mutableStateOf(initial?.code ?: seedCode) }

    fun save(): String {
        val id = currentId
        val savedId = if (id == null) {
            val created = store.create(name, code, initial?.desc ?: seedDesc)
            currentId = created.id
            created.id
        } else {
            store.update(ScriptItem(id, name, code, initial?.updatedAt ?: 0, initial?.desc ?: seedDesc))
            id
        }
        savedName = name
        savedCode = code
        refreshWidgetsAsync(context)
        return savedId
    }

    fun doPaste() {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val text = cm.primaryClip?.takeIf { it.itemCount > 0 }
            ?.getItemAt(0)?.coerceToText(context)?.toString()
        if (!text.isNullOrEmpty()) {
            if (code.trim().length > 50 && text != code) {
                // Guard against wiping real work with a stray clipboard.
                pendingPaste = text
            } else {
                code = text
                pasteNote = "貼り付けました（${text.length}文字）"
            }
        } else {
            pasteNote = "クリップボードが空です"
        }
    }

    fun isDirty(): Boolean = name != savedName || code != savedCode

    fun backWithSave() {
        if (isDirty()) save()
        onBack()
    }
    BackHandler { backWithSave() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("スクリプト編集") },
                navigationIcon = { TextButton(onClick = { backWithSave() }) { Text("← 一覧") } },
                actions = {
                    IconButton(onClick = { editorMenu = true }) {
                        Text("⋮", style = MaterialTheme.typography.titleLarge)
                    }
                    DropdownMenu(expanded = editorMenu, onDismissRequest = { editorMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("📋 クリップボードから貼る") },
                            onClick = { editorMenu = false; doPaste() },
                        )
                        if (currentId != null) {
                            DropdownMenuItem(
                                text = { Text("削除") },
                                onClick = { editorMenu = false; deleteConfirm = true },
                            )
                        }
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing),
        ) {
            Text(
                "書く → ▶ 実行プレビュー → ホーム画面に追加、の順で完成です",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("スクリプト名") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("JavaScript") },
                modifier = Modifier.fillMaxWidth().height(340.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    // Preview always runs the current text and saves first when
                    // dirty, so there is no save button: this line states the
                    // save state instead of asking for an action.
                    if (isDirty()) "編集中（実行・戻るで自動保存されます）" else "✓ 保存済み",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text("${code.length} 文字", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            pasteNote?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    enabled = !running,
                    onClick = {
                        // Previewing must not rewrite an unchanged script.
                        val id = if (currentId == null || isDirty()) save() else currentId!!
                        running = true
                        // Keep the previous preview on screen while re-running,
                        // and always restore the button — a run that throws or
                        // stalls must surface as an error, never a dead button.
                        scope.launch {
                            try {
                                result = kotlinx.coroutines.withTimeoutOrNull(20_000) {
                                    // Detached (engine-owned job): a plain
                                    // child call would trap this timeout if
                                    // the run stalls in native code, and the
                                    // button would never recover.
                                    engine.runDetached(id, code).await()
                                } ?: RunResult.Error(
                                    "実行が20秒以内に終わりませんでした。通信や処理が詰まっている可能性があります。もう一度押してください。",
                                    null, emptyList(),
                                )
                            } catch (e: Exception) {
                                result = RunResult.Error("実行に失敗しました: ${e.message}", null, emptyList())
                            } finally {
                                running = false
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    if (running) {
                        // A spinning indicator, not just greyed text: a run
                        // that takes seconds must visibly read as "working"
                        // (user direction, 2026-11-15).
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("実行中…")
                    } else {
                        Text("▶ 実行プレビュー")
                    }
                }
                OutlinedButton(
                    onClick = {
                        // Always available, even for a brand-new script: save
                        // first so the config screen can preselect it.
                        val id = if (currentId == null || isDirty()) save() else currentId!!
                        pinNote = if (requestPinWidget(context, id)) {
                            "ホーム画面への追加を確認してください"
                        } else {
                            "この端末のランチャーでは対応していません"
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) { Text("ホーム画面に追加") }
            }
            pinNote?.let { Text(it, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall) }

            when (val r = result) {
                is RunResult.Success -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("プレビュー", style = MaterialTheme.typography.titleMedium)
                        TextButton(onClick = { bigPreview = r }) { Text("大きく見る") }
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(Tokens.CardRadius),
                    ) {
                        WidgetPreview(r.tree, Modifier.fillMaxWidth().padding(8.dp))
                    }
                    if (r.logs.isNotEmpty()) {
                        Text("ログ", style = MaterialTheme.typography.titleSmall)
                        Text(r.logs.joinToString("\n"), style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace))
                    }
                }
                is RunResult.Error -> {
                    val errorLineText = r.line?.let { ln -> code.lines().getOrNull(ln - 1)?.trim() }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(Tokens.CardRadius),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("エラー", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.titleSmall)
                            Text(
                                (r.line?.let { "${it}行目: " } ?: "") + r.message,
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                            )
                            if (!errorLineText.isNullOrEmpty()) {
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "問題の文: $errorLineText",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                )
                            }
                            if (r.logs.isNotEmpty()) {
                                Spacer(Modifier.height(4.dp))
                                Text(r.logs.joinToString("\n"), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
                null -> {}
            }
        }
    }

    pendingPaste?.let { text ->
        AlertDialog(
            onDismissRequest = { pendingPaste = null },
            title = { Text("上書きしますか") },
            text = { Text("今の内容（${code.length}文字）を、クリップボードの内容（${text.length}文字）で置き換えます。") },
            confirmButton = {
                TextButton(onClick = {
                    code = text
                    pasteNote = "貼り付けました（${text.length}文字）"
                    pendingPaste = null
                }) { Text("置き換える") }
            },
            dismissButton = { TextButton(onClick = { pendingPaste = null }) { Text("キャンセル") } },
        )
    }
    if (deleteConfirm) {
        AlertDialog(
            onDismissRequest = { deleteConfirm = false },
            title = { Text("削除しますか") },
            text = { Text("「$name」を削除します。元に戻せません。") },
            confirmButton = {
                TextButton(onClick = {
                    deleteConfirm = false
                    currentId?.let { store.delete(it) }
                    refreshWidgetsAsync(context)
                    onBack()
                }) { Text("削除") }
            },
            dismissButton = { TextButton(onClick = { deleteConfirm = false }) { Text("キャンセル") } },
        )
    }

    bigPreview?.let { bp ->
        Dialog(onDismissRequest = { bigPreview = null }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Tokens.CardRadius),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("プレビュー（大）", style = MaterialTheme.typography.titleMedium)
                        TextButton(onClick = { bigPreview = null }) { Text("閉じる") }
                    }
                    WidgetPreview(bp.tree, Modifier.fillMaxWidth().height(430.dp).padding(8.dp))
                }
            }
        }
    }
}
