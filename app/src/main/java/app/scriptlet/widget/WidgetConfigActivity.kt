package app.scriptlet.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.AppWidgetId
import androidx.lifecycle.lifecycleScope
import app.scriptlet.engine.RunResult
import app.scriptlet.engine.ScriptEngine
import app.scriptlet.engine.WidgetNode
import app.scriptlet.scriptSummary
import app.scriptlet.ui.Tokens
import app.scriptlet.ui.WidgetPreview
import kotlinx.coroutines.launch

/** Shown when a widget is placed: pick the script and the frame customization for this widget instance. */
class WidgetConfigActivity : ComponentActivity() {

    // Glance 1.1.1 marks AppWidgetId @RestrictTo(LIBRARY_GROUP), yet building
    // one from the raw id is the only public way to refresh a specific widget
    // instance after configuring it — targeted suppression, with the reason.
    @android.annotation.SuppressLint("RestrictedApi")
    private suspend fun refreshWidget(appWidgetId: Int) {
        if (WidgetKinds.providerClassName(this, appWidgetId) ==
            AnimWidgetReceiver::class.java.name
        ) {
            // Animation lane: render frame 0 right away, then hand over to
            // the frame-swap service (started here, in the foreground, which
            // is the one reliable FGS start point).
            val engine = ScriptEngine(app.scriptlet.data.StoreProvider.get(this))
            when (val outcome = AnimRenderer.renderOnce(this, engine, appWidgetId, 0, 0)) {
                is AnimOutcome.Frame -> AnimRenderer.push(this, appWidgetId, outcome.bitmap)
                is AnimOutcome.Still -> AnimRenderer.push(this, appWidgetId, outcome.bitmap)
                AnimOutcome.Failed -> Unit
            }
            WidgetAnimator.start(this)
            return
        }
        WidgetKinds.widgetFor(this, appWidgetId).update(this, AppWidgetId(appWidgetId))
    }

    companion object {
        /** Extra carrying the script id when the widget was pinned from inside the app. */
        const val EXTRA_SCRIPT_ID = "script_id"
    }

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID,
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        // If the user backs out, the widget must not be added.
        setResult(Activity.RESULT_CANCELED)
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        val store = app.scriptlet.data.StoreProvider.get(this)
        val engine = ScriptEngine(store)
        val config = WidgetConfigStore(this)
        // First-time setup (this widget had no script yet) counts as
        // "adding": after deciding, go straight to the home screen so the
        // user sees the widget where it landed (user request, 2026-11-17).
        // Re-configuring an already-set widget stays in the app.
        val wasConfigured = config.scriptIdFor(appWidgetId) != null
        val preselectId = intent?.extras?.getString(EXTRA_SCRIPT_ID)
            ?.takeIf { id -> store.scripts.value.any { it.id == id } }
            ?: config.scriptIdFor(appWidgetId)
        val existing = config.optionsFor(appWidgetId)

        val bgOptions: List<Pair<String, String?>> = listOf(
            "なし" to null,
            "白" to "#ffffff",
            "黒" to "#111827",
            "ピンク" to "#fce7f1",
            "紫" to "#f1e6f7",
            "水色" to "#e0f2fb",
            "紺" to "#1f2937",
        )
        val padOptions = listOf("なし" to 0, "ふつう" to 8, "広め" to 16)
        val cornerOptions = listOf("なし" to 0, "ふつう" to 12, "丸め" to 24)
        val scaleOptions = listOf("ふつう" to 1f, "大きめ" to 1.2f, "最大" to 1.4f)

        setContent {
            app.scriptlet.ui.ScriptletTheme {
                val scripts by store.scripts.collectAsState()
                // rememberSaveable, not remember: with plain remember, a
                // rotation or process recreation silently reset the picks
                // back to the preselection, and deciding then saved a
                // different script than the one the user had tapped.
                var selectedId by rememberSaveable { mutableStateOf(preselectId) }
                var bgIdx by rememberSaveable {
                    mutableIntStateOf(bgOptions.indexOfFirst { it.second == existing.bgHex }.coerceAtLeast(0))
                }
                var padIdx by rememberSaveable {
                    mutableIntStateOf(padOptions.indexOfFirst { it.second == existing.paddingDp }.coerceAtLeast(0))
                }
                var cornerIdx by rememberSaveable {
                    mutableIntStateOf(cornerOptions.indexOfFirst { it.second == existing.cornerDp }.coerceAtLeast(0))
                }
                var scaleIdx by rememberSaveable {
                    mutableIntStateOf(scaleOptions.indexOfFirst { it.second == existing.textScale }.coerceAtLeast(0))
                }

                // Live framed preview: run the selected script once per
                // selection and draw its tree inside the chosen frame, so the
                // look can be checked *before* deciding (the in-app editor
                // preview intentionally stays frame-less).
                var previewTree by remember { mutableStateOf<WidgetNode?>(null) }
                var previewNote by remember { mutableStateOf<String?>(null) }
                LaunchedEffect(selectedId) {
                    val script = scripts.firstOrNull { it.id == selectedId }
                    previewTree = null
                    previewNote = if (script == null) null else "プレビュー実行中…"
                    if (script != null) {
                        when (val r = engine.run(script.id, script.code)) {
                            is RunResult.Success -> {
                                previewTree = r.tree
                                previewNote = null
                            }
                            is RunResult.Error -> previewNote = "エラー: ${r.message}"
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        // No Scaffold here, so consume the system-bar insets
                        // explicitly: without this the title slides under the
                        // status bar (seen on device).
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "このウィジェットに表示するスクリプト",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Spacer(Modifier.height(8.dp))
                        scripts.forEach { script ->
                            val selected = script.id == selectedId
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { selectedId = script.id },
                                shape = RoundedCornerShape(Tokens.CardRadius),
                                border = if (selected) {
                                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                                } else {
                                    null
                                },
                                colors = if (selected) {
                                    CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    )
                                } else {
                                    CardDefaults.cardColors()
                                },
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        (if (selected) "✓ " else "") + script.name,
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                    val summary = scriptSummary(script)
                                    if (summary.isNotEmpty()) {
                                        Text(
                                            summary,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                            }
                        }

                        if (selectedId != null) {
                            Spacer(Modifier.height(16.dp))
                            Text("プレビュー（外枠つき）", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "実際の表示と少し異なる場合があります。",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.height(Tokens.SmallSpacing))
                            val bgHex = bgOptions[bgIdx].second
                            val frameColor = bgHex?.let {
                                runCatching { Color(android.graphics.Color.parseColor(it)) }.getOrNull()
                            }
                            var frame = Modifier.fillMaxWidth().clip(RoundedCornerShape(cornerOptions[cornerIdx].second.dp))
                            if (frameColor != null) frame = frame.background(frameColor)
                            frame = frame.padding(padOptions[padIdx].second.dp)
                            Box(modifier = frame) {
                                val baseDensity = LocalDensity.current
                                CompositionLocalProvider(
                                    LocalDensity provides Density(
                                        baseDensity.density,
                                        baseDensity.fontScale * scaleOptions[scaleIdx].second,
                                    ),
                                ) {
                                    val tree = previewTree
                                    if (tree != null) {
                                        WidgetPreview(tree, Modifier.fillMaxWidth())
                                    } else {
                                        Text(
                                            previewNote ?: "",
                                            style = MaterialTheme.typography.bodyMedium,
                                            // White only on the dark frames; light
                                            // frames keep the default readable tone.
                                            color = if (bgHex == "#111827" || bgHex == "#1f2937") {
                                                Color.White
                                            } else {
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                            },
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))
                        Text("見た目（任意）", style = MaterialTheme.typography.titleLarge)

                        @Composable
                        fun chipRow(
                            label: String,
                            options: List<Pair<String, *>>,
                            selected: Int,
                            onSelect: (Int) -> Unit,
                        ) {
                            Text(label, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                options.forEachIndexed { i, (name, _) ->
                                    FilterChip(
                                        selected = i == selected,
                                        onClick = { onSelect(i) },
                                        label = { Text(name) },
                                    )
                                }
                            }
                        }

                        chipRow("背景色", bgOptions, bgIdx) { bgIdx = it }
                        chipRow("余白", padOptions, padIdx) { padIdx = it }
                        chipRow("角丸", cornerOptions, cornerIdx) { cornerIdx = it }
                        chipRow("文字の大きさ", scaleOptions, scaleIdx) { scaleIdx = it }
                        Spacer(Modifier.height(16.dp))
                    }

                    // The decision stays reachable: fixed at the bottom,
                    // outside the scroll area.
                    Button(
                        enabled = selectedId != null,
                        onClick = {
                            val sid = selectedId ?: return@Button
                            // One synchronous commit for mapping + options:
                            // the choice must be durable before RESULT_OK,
                            // even if the process dies right after deciding.
                            config.setConfig(
                                appWidgetId,
                                sid,
                                WidgetOptions(
                                    bgHex = bgOptions[bgIdx].second,
                                    paddingDp = padOptions[padIdx].second,
                                    cornerDp = cornerOptions[cornerIdx].second,
                                    textScale = scaleOptions[scaleIdx].second,
                                ),
                            )
                            val result = Intent().putExtra(
                                AppWidgetManager.EXTRA_APPWIDGET_ID,
                                appWidgetId,
                            )
                            setResult(Activity.RESULT_OK, result)
                            lifecycleScope.launch {
                                try {
                                    refreshWidget(appWidgetId)
                                } catch (e: Exception) {
                                    // The mapping is saved; the periodic
                                    // update will still pick it up.
                                }
                                if (!wasConfigured) {
                                    // Adding: jump to the home screen so
                                    // the placed widget is right there.
                                    try {
                                        startActivity(
                                            Intent(Intent.ACTION_MAIN)
                                                .addCategory(Intent.CATEGORY_HOME)
                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                                        )
                                    } catch (e: Exception) {
                                        // No launcher to go to: just close.
                                    }
                                }
                                finish()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    ) {
                        Text("この内容で決定")
                    }
                }
            }
        }
    }
}
