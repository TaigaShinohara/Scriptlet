package app.scriptlet.data

import android.content.Context

/**
 * Process-wide single [ScriptStore].
 *
 * The store used to be constructed separately by the main screen, the widget
 * config screen, and every widget update: each construction re-read every file
 * and re-ran the sample-seeding logic, and separate instances could mutate the
 * index from stale in-memory state. One instance per process removes both
 * problems.
 */
object StoreProvider {
    @Volatile
    private var instance: ScriptStore? = null

    fun get(context: Context): ScriptStore =
        instance ?: synchronized(this) {
            instance ?: ScriptStore(context.applicationContext).also { instance = it }
        }
}
