package app.scriptlet.engine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import org.json.JSONArray
import org.json.JSONObject

/**
 * Renders a canvas node's drawing ops (recorded by the JS CanvasHandle) onto a Bitmap.
 * Coordinates are in dp-like units; the bitmap is rendered at [SCALE]x for crispness
 * and displayed at the node's dp size by the renderers.
 */
object CanvasRenderer {
    private const val SCALE = 2f
    private const val MAX_DIM = 800
    // RemoteViews payloads have a hard transaction ceiling (~1MB shared), so a
    // canvas bitmap must stay well under it: cap the pixel area, not just sides.
    private const val MAX_PIXELS = 600_000

    fun render(opsJson: String?, widthDp: Int, heightDp: Int): Bitmap? {
        if (opsJson.isNullOrBlank()) return null
        val dpW = widthDp.coerceIn(1, 512)
        val dpH = heightDp.coerceIn(1, 512)
        var w = (dpW * SCALE).toInt().coerceAtMost(MAX_DIM)
        var h = (dpH * SCALE).toInt().coerceAtMost(MAX_DIM)
        if (w * h > MAX_PIXELS) {
            val shrink = kotlin.math.sqrt(MAX_PIXELS.toDouble() / (w * h))
            w = (w * shrink).toInt().coerceAtLeast(1)
            h = (h * shrink).toInt().coerceAtLeast(1)
        }
        return try {
            val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            // Scale drawing coordinates to the bitmap actually allocated, so a
            // size-clamped canvas shrinks the drawing instead of clipping it.
            canvas.scale(w.toFloat() / dpW, h.toFloat() / dpH)
            val ops = JSONArray(opsJson)
            for (i in 0 until ops.length()) {
                drawOp(canvas, ops.getJSONObject(i))
            }
            bmp
        } catch (_: Exception) {
            null
        }
    }

    private fun parseColor(o: JSONObject): Int? {
        val hex = if (o.has("color") && !o.isNull("color")) o.optString("color") else null
        if (hex.isNullOrBlank()) return null
        return try {
            android.graphics.Color.parseColor(hex)
        } catch (_: Exception) {
            null
        }
    }

    private fun paint(o: JSONObject, style: Paint.Style = Paint.Style.FILL): Paint? {
        val color = parseColor(o) ?: return null
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
            this.style = style
        }
    }

    private fun drawOp(canvas: Canvas, o: JSONObject) {
        when (o.optString("op")) {
            "bg" -> paint(o)?.let { canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), it) }
            "rect" -> paint(o)?.let {
                canvas.drawRect(
                    o.optDouble("x").toFloat(), o.optDouble("y").toFloat(),
                    (o.optDouble("x") + o.optDouble("w")).toFloat(),
                    (o.optDouble("y") + o.optDouble("h")).toFloat(), it,
                )
            }
            "roundRect" -> paint(o)?.let {
                val rect = RectF(
                    o.optDouble("x").toFloat(), o.optDouble("y").toFloat(),
                    (o.optDouble("x") + o.optDouble("w")).toFloat(),
                    (o.optDouble("y") + o.optDouble("h")).toFloat(),
                )
                val r = o.optDouble("r", 4.0).toFloat()
                canvas.drawRoundRect(rect, r, r, it)
            }
            "circle" -> paint(o)?.let {
                canvas.drawCircle(
                    o.optDouble("cx").toFloat(), o.optDouble("cy").toFloat(),
                    o.optDouble("r").toFloat(), it,
                )
            }
            "arc" -> paint(o)?.let {
                val r = o.optDouble("r").toFloat()
                val cx = o.optDouble("cx").toFloat()
                val cy = o.optDouble("cy").toFloat()
                canvas.drawArc(
                    RectF(cx - r, cy - r, cx + r, cy + r),
                    o.optDouble("start").toFloat(), o.optDouble("sweep").toFloat(),
                    true, it,
                )
            }
            "ring" -> {
                val p = paint(o, Paint.Style.STROKE) ?: return
                p.strokeWidth = o.optDouble("width", 8.0).toFloat()
                p.strokeCap = Paint.Cap.ROUND
                val r = o.optDouble("r").toFloat()
                val cx = o.optDouble("cx").toFloat()
                val cy = o.optDouble("cy").toFloat()
                canvas.drawArc(
                    RectF(cx - r, cy - r, cx + r, cy + r),
                    o.optDouble("start").toFloat(), o.optDouble("sweep").toFloat(),
                    false, p,
                )
            }
            "line" -> {
                val p = paint(o, Paint.Style.STROKE) ?: return
                p.strokeWidth = o.optDouble("width", 2.0).toFloat()
                canvas.drawLine(
                    o.optDouble("x1").toFloat(), o.optDouble("y1").toFloat(),
                    o.optDouble("x2").toFloat(), o.optDouble("y2").toFloat(), p,
                )
            }
            "text" -> {
                val p = paint(o) ?: return
                val size = o.optDouble("size", 12.0).toFloat()
                p.textSize = size
                if (o.optBoolean("bold", false)) p.typeface = Typeface.DEFAULT_BOLD
                // y is the top of the text: convert to a baseline via the font ascent.
                val baseline = o.optDouble("y").toFloat() - p.fontMetrics.ascent
                canvas.drawText(o.optString("value"), o.optDouble("x").toFloat(), baseline, p)
            }
        }
    }
}
