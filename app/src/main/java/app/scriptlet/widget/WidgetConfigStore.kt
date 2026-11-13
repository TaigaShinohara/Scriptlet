package app.scriptlet.widget

import android.content.Context

/** Per-widget-instance frame customization, chosen on the widget config screen. */
data class WidgetOptions(
    val bgHex: String? = null, // null = no frame background (script's own look)
    val paddingDp: Int = 0,
    val cornerDp: Int = 0,
    val textScale: Float = 1f,
)

/** Maps each home-screen widget instance (appWidgetId) to the script it runs and its frame options. */
class WidgetConfigStore(context: Context) {
    private val prefs = context.getSharedPreferences("widget_config", Context.MODE_PRIVATE)

    fun scriptIdFor(appWidgetId: Int): String? =
        prefs.getString("script_$appWidgetId", null)

    fun setScript(appWidgetId: Int, scriptId: String) {
        prefs.edit().putString("script_$appWidgetId", scriptId).apply()
    }

    /**
     * Writes the script mapping and the frame options as ONE synchronous
     * commit. The configure flow used two apply() calls: apply() only queues
     * the disk write, so a process kill right after deciding could lose the
     * mapping entirely — and the widget then had nothing to show. commit()
     * guarantees the choice is durable before RESULT_OK is returned.
     */
    fun setConfig(appWidgetId: Int, scriptId: String, options: WidgetOptions) {
        prefs.edit()
            .putString("script_$appWidgetId", scriptId)
            .putString("opt_bg_$appWidgetId", options.bgHex ?: "")
            .putInt("opt_pad_$appWidgetId", options.paddingDp)
            .putInt("opt_corner_$appWidgetId", options.cornerDp)
            .putFloat("opt_scale_$appWidgetId", options.textScale)
            .commit()
    }

    fun optionsFor(appWidgetId: Int): WidgetOptions = WidgetOptions(
        bgHex = prefs.getString("opt_bg_$appWidgetId", null)?.takeIf { it.isNotEmpty() },
        paddingDp = prefs.getInt("opt_pad_$appWidgetId", 0),
        cornerDp = prefs.getInt("opt_corner_$appWidgetId", 0),
        textScale = prefs.getFloat("opt_scale_$appWidgetId", 1f),
    )

    fun setOptions(appWidgetId: Int, options: WidgetOptions) {
        prefs.edit()
            .putString("opt_bg_$appWidgetId", options.bgHex ?: "")
            .putInt("opt_pad_$appWidgetId", options.paddingDp)
            .putInt("opt_corner_$appWidgetId", options.cornerDp)
            .putFloat("opt_scale_$appWidgetId", options.textScale)
            .apply()
    }

    fun remove(appWidgetId: Int) {
        prefs.edit()
            .remove("script_$appWidgetId")
            .remove("opt_bg_$appWidgetId")
            .remove("opt_pad_$appWidgetId")
            .remove("opt_corner_$appWidgetId")
            .remove("opt_scale_$appWidgetId")
            .apply()
    }
}
