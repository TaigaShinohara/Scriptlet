package app.scriptlet.widget

import android.content.ComponentName
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
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import app.scriptlet.data.ScriptItem
import app.scriptlet.engine.RunResult
import app.scriptlet.engine.ScriptEngine
import kotlinx.coroutines.withTimeoutOrNull

/** Tap on the widget re-runs its script. */
class RefreshAction : ActionCallback {
    @android.annotation.SuppressLint("RestrictedApi") // AppWidgetId: see BaseScriptletWidget
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val appWidgetId = (glanceId as? AppWidgetId)?.appWidgetId ?: return
        // The instance that owns this widget id depends on which size kind it
        // is; updating through the wrong class silently does nothing.
        WidgetKinds.widgetFor(context, appWidgetId).update(context, glanceId)
    }
}

/** Tap on an unconfigured widget opens its own config screen. */
class ConfigureAction : ActionCallback {
    @android.annotation.SuppressLint("RestrictedApi") // AppWidgetId: see BaseScriptletWidget
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val appWidgetId = (glanceId as? AppWidgetId)?.appWidgetId ?: return
        val intent = android.content.Intent(context, WidgetConfigActivity::class.java)
            .putExtra(android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            .addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

/** The three widget size kinds and their receivers, in picker order. */
object WidgetKinds {
    val receiverClasses: List<Class<out BaseScriptletWidgetReceiver>> = listOf(
        ScriptletWidgetSmallReceiver::class.java,
        ScriptletWidgetReceiver::class.java,
        ScriptletWidgetLargeReceiver::class.java,
    )

    /** The provider class name that owns [appWidgetId], or null when unknown. */
    fun providerClassName(context: Context, appWidgetId: Int): String? = try {
        android.appwidget.AppWidgetManager.getInstance(context)
            .getAppWidgetInfo(appWidgetId)?.provider?.className
    } catch (t: Throwable) {
        null
    }

    /** The widget implementation that owns [appWidgetId], by its provider. */
    fun widgetFor(context: Context, appWidgetId: Int): GlanceAppWidget {
        val providerClass = providerClassName(context, appWidgetId)
        return when (providerClass) {
            ScriptletWidgetSmallReceiver::class.java.name -> ScriptletWidgetSmall()
            ScriptletWidgetLargeReceiver::class.java.name -> ScriptletWidgetLarge()
            else -> ScriptletWidget()
        }
    }
}

// 18s: long enough for a slow weather fetch on a real device network
// (the 12s cap made the widget show a timeout error while the same script
// succeeded in the editor), short enough that the launcher never gives up
// on the render — failures degrade to the guarded placeholder instead.
private const val WIDGET_RUN_BUDGET_MS = 18_000L

/** Everything provideGlance needs, computed before composition starts. */
private class PreparedWidget(
    val script: ScriptItem?,
    val mappedId: String?,
    val result: RunResult?,
    val images: Map<String, android.graphics.Bitmap>,
    val options: WidgetOptions,
)

abstract class BaseScriptletWidget : GlanceAppWidget() {

    // Glance 1.1.1 marks AppWidgetId @RestrictTo(LIBRARY_GROUP), yet casting the
    // GlanceId to it is the only public way to learn which widget instance is
    // being rendered (Glance's own samples do exactly this) — hence the
    // targeted suppression rather than a workaround.
    @android.annotation.SuppressLint("RestrictedApi")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = (id as? AppWidgetId)?.appWidgetId
        // NOTHING may escape provideGlance. The launcher also runs this code
        // to render the picker's live preview, and any throwable — including
        // Errors, not just Exceptions — makes the launcher report
        // 「ウィジェットを追加できませんでした」 and refuse the widget.
        // So the whole preparation phase is guarded, and a failure degrades
        // to a plain tappable placeholder instead of a dead widget.
        val prepared: PreparedWidget? = try {
            prepare(context, appWidgetId)
        } catch (t: Throwable) {
            null
        }
        if (prepared == null) {
            provideContent {
                Box(
                    modifier = GlanceModifier.fillMaxSize().clickable(
                        actionStartActivity(
                            ComponentName(context, app.scriptlet.MainActivity::class.java),
                        ),
                    ),
                ) {
                    Text("Scriptlet")
                }
            }
            return
        }
        val script = prepared.script
        val result = prepared.result
        val options = prepared.options
        provideContent {
            var frame: GlanceModifier = GlanceModifier.fillMaxSize()
            val bgColor = options.bgHex?.let {
                try {
                    androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(it))
                } catch (_: Throwable) {
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
            // A configured widget re-runs on tap. An unconfigured one opens
            // its OWN config screen (for this exact widget id), so a widget
            // whose launcher configure flow never completed — or whose
            // script was deleted — can be fixed in place, without the
            // delete-and-re-add dance the old placeholder demanded.
            val tapAction = if (script != null) {
                frame.clickable(actionRunCallback<RefreshAction>())
            } else if (appWidgetId != null) {
                frame.clickable(actionRunCallback<ConfigureAction>())
            } else {
                frame.clickable(
                    actionStartActivity(
                        ComponentName(context, app.scriptlet.MainActivity::class.java),
                    ),
                )
            }
            Box(modifier = tapAction) {
                when {
                    script != null && result is RunResult.Success ->
                        GlanceTree(result.tree, prepared.images, textScale = options.textScale)
                    script != null && result is RunResult.Error -> Text(
                        text = "エラー: ${result.message}" +
                            (result.line?.let { "（${it}行目）" } ?: ""),
                        maxLines = 6,
                    )
                    script != null -> Text("Scriptlet")
                    appWidgetId == null -> Text("Scriptlet")
                    prepared.mappedId == null -> Text(
                        "未設定のウィジェットです。タップで表示するスクリプトを選べます。",
                        maxLines = 6,
                    )
                    else -> Text(
                        "設定されたスクリプトが見つかりません（削除されたようです）。" +
                            "タップで選び直せます。",
                        maxLines = 6,
                    )
                }
            }
        }
    }

    private suspend fun prepare(context: Context, appWidgetId: Int?): PreparedWidget {
        val store = app.scriptlet.data.StoreProvider.get(context)
        val config = WidgetConfigStore(context)
        val scripts = store.scripts.value
        // The per-instance mapping is the ONLY source of truth for what this
        // widget shows. There is deliberately no fallback to another script:
        // an unconfigured instance used to render whichever script had been
        // edited most recently, so placed widgets silently turned into
        // different widgets than the one chosen for them.
        val mappedId = appWidgetId?.let { config.scriptIdFor(it) }
        val script = mappedId?.let { mid -> scripts.firstOrNull { it.id == mid } }

        val result: RunResult? = if (script != null) {
            // Run on the engine's own scope with a hard budget: a slow fetch
            // must never hold the Glance session open until the launcher
            // gives up on the widget (that, too, surfaces as
            // 「追加できませんでした」). The detached run keeps going in the
            // background; its result is simply not used for this render.
            try {
                withTimeoutOrNull(WIDGET_RUN_BUDGET_MS) {
                    ScriptEngine(store).runDetached(script.id, script.code).await()
                } ?: RunResult.Error(
                    "読み込みが制限時間を超えました。タップで再実行できます",
                    null,
                    emptyList(),
                )
            } catch (t: Throwable) {
                RunResult.Error("実行に失敗: ${t.message}", null, emptyList())
            }
        } else {
            null
        }

        val images = if (result is RunResult.Success) {
            try {
                app.scriptlet.engine.ImageResolver.resolveAll(result.tree)
            } catch (t: Throwable) {
                emptyMap()
            }
        } else {
            emptyMap()
        }

        val options = appWidgetId?.let { config.optionsFor(it) } ?: WidgetOptions()
        return PreparedWidget(script, mappedId, result, images, options)
    }
}

/** Standard kind: 4×2. The original widget — existing placements keep working. */
class ScriptletWidget : BaseScriptletWidget() {
    override val sizeMode: SizeMode =
        SizeMode.Responsive(setOf(DpSize(160.dp, 100.dp), DpSize(250.dp, 140.dp)))
}

/** Small kind: 2×2, for one-glance numbers and clocks. */
class ScriptletWidgetSmall : BaseScriptletWidget() {
    override val sizeMode: SizeMode =
        SizeMode.Responsive(setOf(DpSize(110.dp, 110.dp), DpSize(160.dp, 110.dp)))
}

/** Large kind: 4×4, for calendars, charts and boards. */
class ScriptletWidgetLarge : BaseScriptletWidget() {
    override val sizeMode: SizeMode =
        SizeMode.Responsive(setOf(DpSize(250.dp, 140.dp), DpSize(340.dp, 260.dp)))
}

abstract class BaseScriptletWidgetReceiver : GlanceAppWidgetReceiver() {
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        val config = WidgetConfigStore(context)
        appWidgetIds.forEach { config.remove(it) }
    }
}

class ScriptletWidgetReceiver : BaseScriptletWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ScriptletWidget()
}

class ScriptletWidgetSmallReceiver : BaseScriptletWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ScriptletWidgetSmall()
}

class ScriptletWidgetLargeReceiver : BaseScriptletWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ScriptletWidgetLarge()
}
