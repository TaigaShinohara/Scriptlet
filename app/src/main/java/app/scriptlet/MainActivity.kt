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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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

    when {
        !onboardingDone -> app.scriptlet.ui.OnboardingScreen(
            engine = engine,
            onFinish = { name, code, desc ->
                prefs.edit().putInt("onboarding_version", 3).apply()
                onboardingDone = true
                createSeed = Triple(name, code, desc)
            },
            onSkip = {
                prefs.edit().putInt("onboarding_version", 3).apply()
                onboardingDone = true
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
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListScreen(store: ScriptStore, onOpen: (String) -> Unit, onNew: (String, String, String) -> Unit) {
    val context = LocalContext.current
    val scripts by store.scripts.collectAsState()
    val fmt = remember { SimpleDateFormat("MM/dd HH:mm", Locale.JAPAN) }
    var showChooser by remember { mutableStateOf(false) }
    var pinNote by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Scriptlet") }) },
        floatingActionButton = {
            androidx.compose.material3.ExtendedFloatingActionButton(
                onClick = { showChooser = true },
                icon = { Text("＋", style = MaterialTheme.typography.titleLarge) },
                text = { Text("新規") },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing),
        ) {
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
                    onDuplicate = { store.create(item.name + " のコピー", item.code, item.desc) },
                    onRename = { newName -> store.update(item.copy(name = newName)) },
                    onDelete = { store.delete(item.id) },
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
        AlertDialog(
            onDismissRequest = { showChooser = false },
            title = { Text("新しいスクリプト") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    @Composable
                    fun sampleEntry(index: Int) {
                        val (sampleName, sampleCode) = SampleScripts.all[index]
                        val sampleDesc = SampleScripts.descs.getOrElse(index) { "" }
                        Column(
                            modifier = Modifier.fillMaxWidth().clickable {
                                showChooser = false
                                onNew(sampleName, sampleCode, sampleDesc)
                            }.padding(vertical = Tokens.SmallSpacing),
                        ) {
                            Text(sampleName, style = MaterialTheme.typography.titleSmall)
                            if (sampleDesc.isNotEmpty()) {
                                Text(
                                    sampleDesc,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                    Text("おすすめ", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    SampleScripts.all.indices
                        .filter { SampleScripts.keys[it] in SampleScripts.featuredKeys }
                        .forEach { sampleEntry(it) }
                    Spacer(Modifier.height(8.dp))
                    Text("基本サンプル", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    SampleScripts.all.indices
                        .filter { SampleScripts.keys[it] !in SampleScripts.featuredKeys }
                        .forEach { sampleEntry(it) }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = {
                        showChooser = false
                        onNew("新しいスクリプト", "// ここにスクリプトを書く\nconst widget = new ListWidget();\n", "")
                    }) { Text("空白のスクリプト（まっさらから書く）") }
                }
            },
            confirmButton = {
                TextButton(onClick = { showChooser = false }) { Text("キャンセル") }
            },
        )
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
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                val summary = scriptSummary(item)
                if (summary.isNotEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                    engine.run(id, code)
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
                ) { Text(if (running) "実行中…" else "▶ 実行プレビュー") }
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
