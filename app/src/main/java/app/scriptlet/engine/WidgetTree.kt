package app.scriptlet.engine

import org.json.JSONArray
import org.json.JSONObject

/** The widget rendering tree produced by a script. Mirrors the JS-side node objects 1:1. */
data class WidgetNode(
    val type: String, // widget | stack | text | spacer | image | divider
    val children: List<WidgetNode> = emptyList(),
    val value: String? = null, // text content
    val direction: String = "vertical", // stack direction
    val backgroundColor: String? = null,
    val padding: List<Int> = listOf(0, 0, 0, 0), // top, left, bottom, right (dp)
    val spacing: Int = 0,
    val cornerRadius: Int = 0,
    val fontSize: Int = 14,
    val bold: Boolean = false,
    val color: String? = null,
    val align: String = "left",
    val length: Int = 0, // spacer length (dp); -1 = flexible (fills remaining space)
    val width: Int = 0, // explicit width (dp); 0 = auto
    val height: Int = 0, // explicit height (dp); 0 = auto
    val alignItems: String = "start", // container cross-axis alignment: start | center | end
    val imageSource: String? = null, // image node: URL or base64 payload
    val imageKind: String = "url", // image node: url | base64
    val opsJson: String? = null, // canvas node: drawing ops as a JSON array string
    val progressValue: Float = -1f, // progress node: 0..1 (-1 = not a progress node)
    val linkUrl: String? = null, // text node: tap opens this URL
) {
    companion object {
        // Defensive limits: a script builds this tree, so hostile or buggy output
        // must never be able to blow the stack (deep nesting), exhaust memory
        // (node/text volume), or produce absurd layout values (negative sp, huge dp).
        private const val MAX_DEPTH = 32
        private const val MAX_NODES = 2_000
        private const val MAX_TEXT_CHARS = 10_000

        fun fromJson(json: String): WidgetNode {
            val counter = intArrayOf(0)
            return parse(JSONObject(json), depth = 0, counter = counter)
        }

        private fun parse(o: JSONObject, depth: Int, counter: IntArray): WidgetNode {
            counter[0]++
            val children = mutableListOf<WidgetNode>()
            val arr: JSONArray? = o.optJSONArray("children")
            if (arr != null && depth < MAX_DEPTH && counter[0] < MAX_NODES) {
                for (i in 0 until arr.length()) {
                    if (counter[0] >= MAX_NODES) break
                    children += parse(arr.getJSONObject(i), depth + 1, counter)
                }
            }
            val pad = mutableListOf(0, 0, 0, 0)
            val padArr: JSONArray? = o.optJSONArray("padding")
            if (padArr != null) {
                for (i in 0 until minOf(4, padArr.length())) {
                    pad[i] = padArr.optInt(i, 0).coerceIn(0, 64)
                }
            }
            return WidgetNode(
                type = o.optString("type", "widget"),
                children = children,
                value = if (o.has("value")) o.optString("value").take(MAX_TEXT_CHARS) else null,
                direction = o.optString("direction", "vertical"),
                backgroundColor = if (o.has("backgroundColor") && !o.isNull("backgroundColor")) o.optString("backgroundColor") else null,
                padding = pad,
                spacing = o.optInt("spacing", 0).coerceIn(0, 64),
                cornerRadius = o.optInt("cornerRadius", 0).coerceIn(0, 64),
                fontSize = o.optInt("fontSize", 14).coerceIn(6, 72),
                bold = o.optBoolean("bold", false),
                color = if (o.has("color") && !o.isNull("color")) o.optString("color") else null,
                align = o.optString("align", "left"),
                length = o.optInt("length", 0).coerceIn(-1, 512),
                width = o.optInt("width", 0).coerceIn(0, 1024),
                height = o.optInt("height", 0).coerceIn(0, 1024),
                alignItems = o.optString("alignItems", "start"),
                imageSource = if (o.has("imageSource") && !o.isNull("imageSource")) o.optString("imageSource") else null,
                imageKind = o.optString("imageKind", "url"),
                opsJson = o.optJSONArray("ops")?.toString(),
                progressValue = if (o.has("progress")) o.optDouble("progress", -1.0).toFloat() else -1f,
                linkUrl = if (o.has("url") && !o.isNull("url")) o.optString("url") else null,
            )
        }
    }
}
