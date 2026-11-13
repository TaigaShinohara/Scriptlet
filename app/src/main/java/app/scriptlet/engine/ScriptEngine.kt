package app.scriptlet.engine

import app.scriptlet.data.ScriptStore
import com.dokar.quickjs.ExperimentalQuickJsApi
import com.dokar.quickjs.QuickJs
import com.dokar.quickjs.QuickJsException
import com.dokar.quickjs.binding.asyncFunction
import com.dokar.quickjs.binding.function
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.concurrent.Executors
import java.net.HttpURLConnection
import java.net.URL
import java.util.Collections

sealed interface RunResult {
    data class Success(val tree: WidgetNode, val logs: List<String>) : RunResult
    data class Error(val message: String, val line: Int?, val logs: List<String>) : RunResult
}

/** Reads at most [maxBytes] + 1 bytes so an oversized response fails the size check instead of exhausting memory first. */
internal fun InputStream.readCapped(maxBytes: Int): ByteArray {
    val buffer = ByteArray(8192)
    val out = java.io.ByteArrayOutputStream()
    var total = 0
    while (true) {
        val n = read(buffer)
        if (n < 0) break
        total += n
        if (total > maxBytes) break
        out.write(buffer, 0, n)
    }
    return out.toByteArray()
}

@OptIn(ExperimentalQuickJsApi::class)
class ScriptEngine(private val store: ScriptStore) {

    // Runs are serialized and confined to one dedicated thread. QuickJS is a
    // native runtime: creating instances back-to-back on a shared thread pool
    // let a second run stall without ever returning (the preview button then
    // stayed dead). One thread + one run at a time removes that class of
    // failure for the editor, the onboarding trial and widget updates alike.
    private val runMutex = Mutex()
    private val runDispatcher = Executors.newSingleThreadExecutor { r ->
        Thread(r, "scriptlet-js").apply { isDaemon = true }
    }.asCoroutineDispatcher()

    suspend fun run(scriptId: String, code: String): RunResult = runMutex.withLock {
        runInternal(scriptId, code)
    }

    private suspend fun runInternal(scriptId: String, code: String): RunResult = withContext(runDispatcher) {
        // JS callbacks can arrive from QuickJS job threads; keep the log list thread-safe.
        val logs = Collections.synchronizedList(mutableListOf<String>())
        val qjs = try {
            QuickJs.create(runDispatcher)
        } catch (e: Exception) {
            return@withContext RunResult.Error("JSエンジンの初期化に失敗: ${e.message}", null, logs.toList())
        }
        // Error line numbers are relative to (prelude + wrapper + user code). The user
        // code starts after the prelude's lines, the "\n" joining them, and the
        // wrapper's opening line — computed structurally, never by searching for
        // the code inside the source (which breaks if the code text also occurs
        // inside the prelude).
        val lineOffset = Prelude.SOURCE.count { it == '\n' } + 2
        try {
            qjs.evaluationTimeoutMillis = 5_000
            try { qjs.memoryLimit = 32L * 1024 * 1024 } catch (_: Exception) {}

            qjs.function("__log") { args: Array<Any?> ->
                logs += args.joinToString(" ") { it?.toString() ?: "null" }
                null
            }
            qjs.asyncFunction("__fetchText") { args: Array<Any?> ->
                val url = args.getOrNull(0)?.toString()
                    ?: throw IllegalArgumentException("fetch: url is required")
                httpGet(url)
            }
            qjs.function("__storageGet") { args: Array<Any?> ->
                store.kvGet(scriptId, args.getOrNull(0)?.toString() ?: "")
            }
            qjs.function("__storageSet") { args: Array<Any?> ->
                store.kvSet(
                    scriptId,
                    args.getOrNull(0)?.toString() ?: "",
                    args.getOrNull(1)?.toString() ?: "",
                )
                null
            }

            val wrapped = "(async () => {\n" + code +
                "\n;if (typeof widget !== 'undefined') { Script.setWidget(widget); }\n})()"
            val full = Prelude.SOURCE + "\n" + wrapped

            qjs.evaluate<Any?>(full, "script.js", false)
            val treeJson: String? = qjs.evaluate("JSON.stringify(globalThis.__widgetTree)", "host.js", false)
            when {
                treeJson.isNullOrBlank() || treeJson == "null" -> RunResult.Error(
                    "ウィジェットが設定されませんでした。スクリプトの最後で Script.setWidget(widget) を呼ぶか、変数名を widget にしてください。",
                    null, logs.toList(),
                )
                treeJson.length > MAX_TREE_JSON_CHARS -> RunResult.Error(
                    "ウィジェットの内容が大きすぎます（${treeJson.length}文字）。表示する項目を減らしてください。",
                    null, logs.toList(),
                )
                else -> RunResult.Success(WidgetNode.fromJson(treeJson), logs.toList())
            }
        } catch (e: QuickJsException) {
            val adjustedLine = e.lineNumber?.let { it - lineOffset }?.takeIf { it > 0 }
            RunResult.Error(e.message ?: "JSエラー", adjustedLine, logs.toList())
        } catch (e: StackOverflowError) {
            // A pathological script (e.g. self-building trees) must surface as a
            // script error, never take the host process down.
            RunResult.Error("スクリプトの処理が深くなりすぎました（再帰や巨大なツリーの可能性）", null, logs.toList())
        } catch (e: Exception) {
            RunResult.Error(e.message ?: e.toString(), null, logs.toList())
        } finally {
            try { qjs.close() } catch (_: Exception) {}
        }
    }

    private fun httpGet(url: String): String {
        require(url.startsWith("https://") || url.startsWith("http://")) {
            "fetch: http/https のURLだけ使えます"
        }
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.connectTimeout = 10_000
        conn.readTimeout = 10_000
        conn.instanceFollowRedirects = true
        conn.setRequestProperty("User-Agent", "Scriptlet")
        try {
            val code = conn.responseCode
            if (code !in 200..299) {
                // Surface HTTP errors as fetch failures so scripts can catch them,
                // instead of silently returning the error body as if it were data.
                throw IllegalStateException("HTTP $code")
            }
            val bytes = conn.inputStream.use { it.readCapped(MAX_FETCH_BYTES) }
            require(bytes.size <= MAX_FETCH_BYTES) { "Response too large (>512KB)" }
            return bytes.toString(Charsets.UTF_8)
        } finally {
            conn.disconnect()
        }
    }

    private companion object {
        const val MAX_FETCH_BYTES = 512 * 1024
        const val MAX_TREE_JSON_CHARS = 1_000_000
    }
}
