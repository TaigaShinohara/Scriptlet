package app.scriptlet.widget

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.widget.RemoteViews
import app.scriptlet.R
import app.scriptlet.data.StoreProvider
import app.scriptlet.engine.CanvasRenderer
import app.scriptlet.engine.RunResult
import app.scriptlet.engine.ScriptEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Phase A prototype — the "frame barrage" animation lane.
 *
 * Normal widgets render through Glance/RemoteViews, which the launcher only
 * rebuilds on discrete updates; there is no per-frame redraw path. This lane
 * sidesteps that: a dedicated provider whose RemoteViews hold ONE ImageView,
 * and a foreground service that re-runs the script per frame, rasterizes its
 * canvas through [CanvasRenderer], and swaps the bitmap via
 * [AppWidgetManager.updateAppWidget]. Deliberately separate from the Glance
 * widgets so the experiment can never destabilize them.
 *
 * Scope limits (Phase A, by design):
 *  - Only canvas-only scripts animate (the tree's single child is a canvas).
 *  - Scripts opt in with Script.animate({fps}); without it the widget shows
 *    frame 0 as a still image.
 *  - The loop runs only while the screen is on, and the service stops itself
 *    when no animation widgets remain.
 */

private const val ANIM_RUN_BUDGET_MS = 3_000L

/** What one render attempt produced for an animation widget. */
sealed interface AnimOutcome {
    /** A live frame; keep animating at [fps]. */
    data class Frame(val bitmap: Bitmap, val fps: Int) : AnimOutcome

    /** A still image (placeholder or non-animated script); push once, recheck rarely. */
    data class Still(val bitmap: Bitmap) : AnimOutcome

    /** Transient failure; keep the previous frame. */
    data object Failed : AnimOutcome
}

object AnimRenderer {

    /** The single canvas a script may animate, or null when it is not canvas-only. */
    private fun soleCanvas(tree: app.scriptlet.engine.WidgetNode) =
        when {
            tree.type == "canvas" -> tree
            tree.children.size == 1 && tree.children[0].type == "canvas" -> tree.children[0]
            else -> null
        }

    suspend fun renderOnce(
        context: Context,
        engine: ScriptEngine,
        appWidgetId: Int,
        frame: Int,
        elapsedMs: Long,
    ): AnimOutcome {
        val store = StoreProvider.get(context)
        val config = WidgetConfigStore(context)
        val mappedId = config.scriptIdFor(appWidgetId)
            ?: return AnimOutcome.Still(placeholder(context, "タップで表示するスクリプトを選べます"))
        val script = store.scripts.value.firstOrNull { it.id == mappedId }
            ?: return AnimOutcome.Still(placeholder(context, "スクリプトが見つかりません。タップで選び直せます"))
        val result = try {
            withTimeoutOrNull(ANIM_RUN_BUDGET_MS) {
                engine.runDetached(script.id, script.code, frame, elapsedMs).await()
            }
        } catch (t: Throwable) {
            null
        }
        return when (result) {
            is RunResult.Success -> {
                val canvas = soleCanvas(result.tree)
                    ?: return AnimOutcome.Still(
                        placeholder(context, "アニメはCanvasだけのスクリプト専用です"),
                    )
                val bmp = CanvasRenderer.render(canvas.opsJson, canvas.width, canvas.height)
                    ?: return AnimOutcome.Failed
                val fps = result.animationFps
                if (fps != null) {
                    AnimOutcome.Frame(bmp, fps)
                } else {
                    AnimOutcome.Still(bmp)
                }
            }
            else -> AnimOutcome.Failed
        }
    }

    /** Builds the RemoteViews for one animation widget showing [bitmap]. */
    fun remoteViews(context: Context, appWidgetId: Int, bitmap: Bitmap): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_anim)
        views.setImageViewBitmap(R.id.anim_image, bitmap)
        val intent = Intent(context, WidgetConfigActivity::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pending = PendingIntent.getActivity(
            context,
            appWidgetId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        views.setOnClickPendingIntent(R.id.anim_image, pending)
        return views
    }

    fun push(context: Context, appWidgetId: Int, bitmap: Bitmap) {
        try {
            AppWidgetManager.getInstance(context)
                .updateAppWidget(appWidgetId, remoteViews(context, appWidgetId, bitmap))
        } catch (_: Throwable) {
            // A vanished widget or an oversized bitmap must never kill the loop.
        }
    }

    /** Soft placeholder card for states the user resolves by tapping. */
    fun placeholder(context: Context, message: String): Bitmap {
        val w = 440
        val h = 260
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val bg = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#FCE7F1") }
        canvas.drawRoundRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), 36f, 36f, bg)
        val text = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#3D2C34")
            textSize = 30f
            textAlign = Paint.Align.CENTER
        }
        // Crude two-line wrap: the messages are short and fixed.
        val words = message.split("。")
        val lines = words.filter { it.isNotBlank() }.map { if (it.endsWith("。")) it else "$it。" }
        val startY = h / 2f - (lines.size - 1) * 22f
        lines.forEachIndexed { i, line ->
            canvas.drawText(line, w / 2f, startY + i * 44f, text)
        }
        return bmp
    }
}

/** Starts/stops the animation service; safe to call from anywhere. */
object WidgetAnimator {
    fun animWidgetIds(context: Context): IntArray =
        AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, AnimWidgetReceiver::class.java))

    /** Starts the service when at least one animation widget exists. */
    fun start(context: Context) {
        if (animWidgetIds(context).isEmpty()) return
        try {
            val intent = Intent(context, AnimService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        } catch (_: Exception) {
            // Background starts are restricted on Android 12+: the service
            // then starts the next time the app or the config screen is in
            // the foreground (both call here), so this is a defer, not a loss.
        }
    }

    fun stop(context: Context) {
        try {
            context.stopService(Intent(context, AnimService::class.java))
        } catch (_: Exception) {
        }
    }
}

class AnimWidgetReceiver : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        val pending = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            try {
                val engine = ScriptEngine(StoreProvider.get(context))
                for (id in appWidgetIds) {
                    when (val outcome = AnimRenderer.renderOnce(context, engine, id, 0, 0)) {
                        is AnimOutcome.Frame -> AnimRenderer.push(context, id, outcome.bitmap)
                        is AnimOutcome.Still -> AnimRenderer.push(context, id, outcome.bitmap)
                        AnimOutcome.Failed -> AnimRenderer.push(
                            context, id,
                            AnimRenderer.placeholder(context, "読み込み中…"),
                        )
                    }
                }
                WidgetAnimator.start(context)
            } finally {
                pending.finish()
            }
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // The service loop re-reads the live id set every tick and stops
        // itself when none remain; nothing to clean up per instance.
    }
}

/**
 * The frame-barrage loop. Foreground service (type specialUse — the only
 * type with no cumulative runtime cap; dataSync would be throttled to
 * 6h/24h on Android 15+). Runs only while the screen is on.
 */
class AnimService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var loopJob: Job? = null
    private var engine: ScriptEngine? = null

    @Volatile private var screenOn = true

    private class WidgetState {
        var frame: Int = 0
        var startUptime: Long = 0
        var intervalMs: Long = 200
        var nextDueUptime: Long = 0
        var lastStill: Bitmap? = null
    }

    private val states = mutableMapOf<Int, WidgetState>()

    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_SCREEN_OFF -> screenOn = false
                Intent.ACTION_SCREEN_ON -> {
                    screenOn = true
                    // Restart each widget's clock so elapsedMs does not jump
                    // by the whole screen-off duration on the next frame.
                    val now = SystemClock.uptimeMillis()
                    states.values.forEach {
                        it.startUptime = now - it.frame * it.intervalMs
                        it.nextDueUptime = now
                    }
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        engine = ScriptEngine(StoreProvider.get(this))
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(screenReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(screenReceiver, filter)
        }
        startForegroundWithNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }
        if (loopJob?.isActive != true) {
            loopJob = scope.launch { loop() }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        try {
            unregisterReceiver(screenReceiver)
        } catch (_: Exception) {
        }
        scope.cancel()
        super.onDestroy()
    }

    private fun startForegroundWithNotification() {
        val channelId = "anim_widget"
        val nm = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm?.createNotificationChannel(
                NotificationChannel(channelId, "アニメーションウィジェット", NotificationManager.IMPORTANCE_LOW),
            )
        }
        val stopIntent = PendingIntent.getService(
            this,
            1,
            Intent(this, AnimService::class.java).setAction(ACTION_STOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val openIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, app.scriptlet.MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val notification: Notification = Notification.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Scriptlet アニメーション")
            .setContentText("ウィジェットをアニメーション中（画面オフで自動停止）")
            .setContentIntent(openIntent)
            .addAction(Notification.Action.Builder(null, "止める", stopIntent).build())
            .setOngoing(true)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIF_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIF_ID, notification)
        }
    }

    private suspend fun loop() {
        var emptyTicks = 0
        while (true) {
            val ids = WidgetAnimator.animWidgetIds(this)
            if (ids.isEmpty()) {
                // Nothing to animate: give stragglers ~10s, then stop.
                if (++emptyTicks > 20) {
                    stopSelf()
                    return
                }
            } else {
                emptyTicks = 0
                states.keys.retainAll(ids.toSet())
                if (screenOn) {
                    val now = SystemClock.uptimeMillis()
                    for (id in ids) {
                        val state = states.getOrPut(id) { WidgetState() }
                        if (now < state.nextDueUptime) continue
                        if (state.startUptime == 0L) state.startUptime = now
                        val elapsed = now - state.startUptime
                        val eng = engine ?: continue
                        when (val outcome = AnimRenderer.renderOnce(this, eng, id, state.frame, elapsed)) {
                            is AnimOutcome.Frame -> {
                                AnimRenderer.push(this, id, outcome.bitmap)
                                state.intervalMs = (1_000L / outcome.fps).coerceIn(100L, 1_000L)
                                state.frame++
                                state.lastStill = null
                                state.nextDueUptime = SystemClock.uptimeMillis() + state.intervalMs
                            }
                            is AnimOutcome.Still -> {
                                // Push only on change: a non-animated canvas
                                // script (e.g. a clock) then behaves like a
                                // slow still widget instead of freezing, and
                                // placeholders are not re-sent every cycle.
                                if (state.lastStill?.sameAs(outcome.bitmap) != true) {
                                    AnimRenderer.push(this, id, outcome.bitmap)
                                    state.lastStill = outcome.bitmap
                                }
                                state.nextDueUptime = now + 30_000L
                            }
                            AnimOutcome.Failed -> {
                                state.nextDueUptime = now + maxOf(state.intervalMs, 1_000L)
                            }
                        }
                    }
                }
            }
            delay(100)
        }
    }

    companion object {
        const val ACTION_STOP = "app.scriptlet.action.ANIM_STOP"
        private const val NOTIF_ID = 4201
    }
}
