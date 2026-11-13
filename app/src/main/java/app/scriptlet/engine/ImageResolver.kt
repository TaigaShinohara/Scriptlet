package app.scriptlet.engine

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/** Resolves image nodes (URL or base64) in a widget tree to decoded bitmaps. */
object ImageResolver {
    private const val MAX_BYTES = 1_500_000
    private const val MAX_DIM = 256

    fun decodeBase64(data: String): Bitmap? = try {
        val bytes = Base64.decode(data, Base64.DEFAULT)
        if (bytes.size > MAX_BYTES) null else scaleDown(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
    } catch (_: Exception) {
        null
    }

    fun fetchUrl(url: String): Bitmap? = try {
        require(url.startsWith("https://") || url.startsWith("http://"))
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.connectTimeout = 5_000
        conn.readTimeout = 8_000
        conn.instanceFollowRedirects = true
        conn.setRequestProperty("User-Agent", "Scriptlet")
        try {
            if (conn.responseCode !in 200..299) {
                null
            } else {
                // Stream-capped read: an oversized image fails instead of
                // exhausting memory before the size check runs.
                val bytes = conn.inputStream.use { it.readCapped(MAX_BYTES) }
                if (bytes.size > MAX_BYTES) null
                else scaleDown(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            }
        } finally {
            conn.disconnect()
        }
    } catch (_: Exception) {
        null
    }

    private fun scaleDown(bmp: Bitmap?): Bitmap? {
        if (bmp == null) return null
        val max = maxOf(bmp.width, bmp.height)
        if (max <= MAX_DIM) return bmp
        val scale = MAX_DIM.toFloat() / max
        return Bitmap.createScaledBitmap(
            bmp,
            (bmp.width * scale).toInt().coerceAtLeast(1),
            (bmp.height * scale).toInt().coerceAtLeast(1),
            true,
        )
    }

    /** Walks the tree and resolves every image node (in parallel). Keys are the node's imageSource strings. */
    suspend fun resolveAll(root: WidgetNode): Map<String, Bitmap> = withContext(Dispatchers.IO) {
        val sources = LinkedHashMap<String, String>() // source -> kind
        fun walk(n: WidgetNode) {
            if (n.type == "image") {
                val src = n.imageSource
                if (!src.isNullOrBlank()) sources[src] = n.imageKind
            }
            n.children.forEach(::walk)
        }
        walk(root)
        coroutineScope {
            sources.map { (src, kind) ->
                async {
                    val bmp = if (kind == "base64") decodeBase64(src) else fetchUrl(src)
                    if (bmp != null) src to bmp else null
                }
            }.awaitAll().filterNotNull().toMap()
        }
    }
}
