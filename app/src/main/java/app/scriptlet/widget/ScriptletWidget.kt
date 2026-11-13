package app.scriptlet.widget

import android.content.Context
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.AppWidgetId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.padding
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.glance.action.clickable
import app.scriptlet.engine.RunResult
import app.scriptlet.engine.ScriptEngine

/** Tap on the widget re-runs its script. */
class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        ScriptletWidget().update(context, glanceId)
    }
}

class ScriptletWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode =
        SizeMode.Responsive(setOf(DpSize(160.dp, 100.dp), DpSize(250.dp, 140.dp)))

    // Glance 1.1.1 marks AppWidgetId @RestrictTo(LIBRARY_GROUP), yet casting the
    // GlanceId to it is the only public way to learn which widget instance is
    // being rendered (Glance's own samples do exactly this) — hence the
    // targeted suppression rather than a workaround.
    @android.annotation.SuppressLint("RestrictedApi")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = (id as? AppWidgetId)?.appWidgetId ?: -1
        val store = app.scriptlet.data.StoreProvider.get(context)
        val config = WidgetConfigStore(context)
        val scripts = store.scripts.value
        val scriptId = config.scriptIdFor(appWidgetId) ?: scripts.firstOrNull()?.id
        val script = scripts.firstOrNull { it.id == scriptId }

        // Last-resort guard: nothing in here may escape provideGlance, or the
        // widget would sit on its placeholder forever with no explanation.
        val result: RunResult? = try {
            if (script != null) {
                ScriptEngine(store).run(script.id, script.code)
            } else {
                null
            }
        } catch (e: Exception) {
            RunResult.Error("実行に失敗: ${e.message}", null, emptyList())
        }

        val images = if (result is RunResult.Success) {
            try {
                app.scriptlet.engine.ImageResolver.resolveAll(result.tree)
            } catch (e: Exception) {
                emptyMap()
            }
        } else {
            emptyMap()
        }

        val options = config.optionsFor(appWidgetId)
        provideContent {
            var frame: GlanceModifier = GlanceModifier.fillMaxSize()
            val bgColor = options.bgHex?.let {
                try {
                    androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(it))
                } catch (_: Exception) {
                    null
                }
            }
            if (bgColor != null) {
                frame = frame.background(androidx.glance.unit.ColorProvider(bgColor))
                if (options.cornerDp > 0) {
                    frame = frame.cornerRadius(options.cornerDp.dp)
                }
            }
            if (options.paddingDp > 0) {
                frame = frame.padding(options.paddingDp.dp)
            }
            Box(
                modifier = frame.clickable(actionRunCallback<RefreshAction>()),
            ) {
                when {
                    script == null -> Text("Scriptlet: スクリプトがありません。アプリで作成してください。")
                    result is RunResult.Success -> GlanceTree(result.tree, images, textScale = options.textScale)
                    result is RunResult.Error -> Text(
                        text = "エラー: ${result.message}" +
                            (result.line?.let { "（${it}行目）" } ?: ""),
                        maxLines = 6,
                    )
                    else -> Text("Scriptlet")
                }
            }
        }
    }
}

class ScriptletWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ScriptletWidget()

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        val config = WidgetConfigStore(context)
        appWidgetIds.forEach { config.remove(it) }
    }
}
