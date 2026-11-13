package app.scriptlet.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.UUID

data class ScriptItem(
    val id: String,
    val name: String,
    val code: String,
    val updatedAt: Long,
    /** One-line summary shown in pickers; empty for user scripts (UI falls back to a code excerpt). */
    val desc: String = "",
)

/** File-based script storage: index.json (id/name/updatedAt) + one .js file per script. */
class ScriptStore(context: Context) {
    // Always the application context: the store outlives any single screen.
    private val dir = File(context.applicationContext.filesDir, "scripts").apply { mkdirs() }
    private val indexFile = File(dir, "index.json")
    private val kvFile = File(dir, "storage-kv.json")

    // Parsed KV file, cached in memory: Storage.get is called from JS in loops,
    // and re-reading + re-parsing the whole file per call made that quadratic.
    private var kvCache: JSONObject? = null

    private val _scripts = MutableStateFlow<List<ScriptItem>>(emptyList())
    val scripts: StateFlow<List<ScriptItem>> = _scripts

    init {
        val seededFile = File(dir, "seeded-samples.json")
        if (!indexFile.exists()) {
            // First launch: seed the sample scripts.
            val now = System.currentTimeMillis()
            val seeds = app.scriptlet.engine.SampleScripts.all.mapIndexed { i, (name, code) ->
                ScriptItem(
                    UUID.randomUUID().toString(), name, code, now - i,
                    app.scriptlet.engine.SampleScripts.descs.getOrElse(i) { "" },
                )
            }
            seeds.forEach { writeCode(it) }
            saveIndex(seeds)
            writeAtomic(seededFile, JSONArray(app.scriptlet.engine.SampleScripts.keys).toString())
        } else {
            // Upgrade: add samples whose key was never seeded (deleted samples stay deleted).
            val seeded: MutableSet<String> = if (seededFile.exists()) {
                try {
                    val arr = JSONArray(seededFile.readText())
                    (0 until arr.length()).map { arr.getString(it) }.toMutableSet()
                } catch (e: Exception) {
                    mutableSetOf("hello", "weekly-calendar", "fetch")
                }
            } else {
                // Installs from before key tracking: the original samples were seeded at first launch.
                mutableSetOf("hello", "weekly-calendar", "fetch")
            }
            val missing = app.scriptlet.engine.SampleScripts.all.filterIndexed { i, _ ->
                app.scriptlet.engine.SampleScripts.keys[i] !in seeded
            }
            if (missing.isNotEmpty()) {
                reload()
                val now = System.currentTimeMillis()
                val added = missing.mapIndexed { i, (name, code) ->
                    val keyIdx = app.scriptlet.engine.SampleScripts.all.indexOfFirst { it.first == name }
                    ScriptItem(
                        UUID.randomUUID().toString(), name, code, now - i,
                        if (keyIdx >= 0) app.scriptlet.engine.SampleScripts.descs.getOrElse(keyIdx) { "" } else "",
                    )
                }
                added.forEach { writeCode(it) }
                saveIndex(_scripts.value + added)
                seeded += app.scriptlet.engine.SampleScripts.keys
                writeAtomic(seededFile, JSONArray(seeded.toList()).toString())
            } else if (!seededFile.exists()) {
                writeAtomic(seededFile, JSONArray(seeded.toList()).toString())
            }
        }
        reload()
    }

    @Synchronized
    fun reload() {
        val items = mutableListOf<ScriptItem>()
        if (indexFile.exists()) {
            try {
                val arr = JSONArray(indexFile.readText())
                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    val id = o.getString("id")
                    val codeFile = File(dir, "$id.js")
                    items += ScriptItem(
                        id = id,
                        name = o.getString("name"),
                        code = if (codeFile.exists()) codeFile.readText() else "",
                        updatedAt = o.optLong("updatedAt", 0),
                        desc = o.optString("desc", ""),
                    )
                }
            } catch (e: Exception) {
                // A corrupt index must not crash every launch: keep it for forensics, start empty.
                runCatching {
                    val backup = File(dir, "index.json.broken")
                    indexFile.copyTo(backup, overwrite = true)
                }
            }
        }
        _scripts.value = items.sortedByDescending { it.updatedAt }
    }

    @Synchronized
    fun create(name: String, code: String, desc: String = ""): ScriptItem {
        // Re-read from disk first: another component may have changed the index
        // since this instance last looked, and mutating from a stale in-memory
        // list would silently roll its changes back.
        reload()
        val item = ScriptItem(UUID.randomUUID().toString(), name, code, System.currentTimeMillis(), desc)
        writeCode(item)
        saveIndex(_scripts.value + item)
        reload()
        return item
    }

    @Synchronized
    fun update(item: ScriptItem) {
        reload()
        val updated = item.copy(updatedAt = System.currentTimeMillis())
        writeCode(updated)
        saveIndex(_scripts.value.map { if (it.id == item.id) updated else it })
        reload()
    }

    @Synchronized
    fun delete(id: String) {
        reload()
        File(dir, "$id.js").delete()
        runCatching {
            val all = loadKv()
            if (all.has(id)) {
                all.remove(id)
                writeAtomic(kvFile, all.toString())
                kvCache = all
            }
        }
        saveIndex(_scripts.value.filter { it.id != id })
        reload()
    }

    // ---- per-script key-value storage (JS Storage API) ----
    private fun loadKv(): JSONObject {
        kvCache?.let { return it }
        val parsed = try {
            if (kvFile.exists()) JSONObject(kvFile.readText()) else JSONObject()
        } catch (e: Exception) {
            JSONObject()
        }
        kvCache = parsed
        return parsed
    }

    @Synchronized
    fun kvGet(scriptId: String, key: String): String? {
        return try {
            val per = loadKv().optJSONObject(scriptId) ?: return null
            if (per.has(key)) per.getString(key) else null
        } catch (e: Exception) {
            null
        }
    }

    @Synchronized
    fun kvSet(scriptId: String, key: String, value: String) {
        try {
            val all = loadKv()
            val per = all.optJSONObject(scriptId) ?: JSONObject().also { all.put(scriptId, it) }
            per.put(key, value)
            writeAtomic(kvFile, all.toString())
            kvCache = all
        } catch (e: Exception) {
            // Storage is best-effort: a failed write must not fail the script run.
            kvCache = null
        }
    }

    /**
     * Writes via a temp file + fsync + rename, so a kill or power loss mid-write
     * can never leave a torn file behind.
     */
    private fun writeAtomic(target: File, text: String) {
        val tmp = File(target.parentFile, target.name + ".tmp")
        java.io.FileOutputStream(tmp).use { out ->
            out.write(text.toByteArray(Charsets.UTF_8))
            out.flush()
            runCatching { out.fd.sync() }
        }
        if (!tmp.renameTo(target)) {
            target.writeText(text)
            tmp.delete()
        }
    }

    private fun writeCode(item: ScriptItem) {
        writeAtomic(File(dir, "${item.id}.js"), item.code)
    }

    private fun saveIndex(items: List<ScriptItem>) {
        val arr = JSONArray()
        items.forEach { item ->
            arr.put(JSONObject().apply {
                put("id", item.id)
                put("name", item.name)
                put("updatedAt", item.updatedAt)
                if (item.desc.isNotEmpty()) put("desc", item.desc)
            })
        }
        writeAtomic(indexFile, arr.toString())
    }
}
