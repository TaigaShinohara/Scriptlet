package app.scriptlet.widget

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ColumnScope
import androidx.glance.layout.Row
import androidx.glance.layout.RowScope
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import app.scriptlet.engine.WidgetNode
import app.scriptlet.ui.Tokens

private fun parseColor(hex: String?): Color? {
    if (hex.isNullOrBlank()) return null
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (_: Exception) {
        null
    }
}

private fun colorProvider(hex: String?): ColorProvider? =
    parseColor(hex)?.let { ColorProvider(it) }

/** Renders a script-produced [WidgetNode] tree as Glance UI (RemoteViews-backed). */
@Composable
fun GlanceTree(
    node: WidgetNode,
    images: Map<String, Bitmap> = emptyMap(),
    textScale: Float = 1f,
) {
    RenderNode(node, parentHorizontal = false, isRoot = true, images = images, textScale = textScale)
}

@Composable
private fun RenderNode(
    node: WidgetNode,
    parentHorizontal: Boolean,
    isRoot: Boolean = false,
    images: Map<String, Bitmap> = emptyMap(),
    textScale: Float = 1f,
) {
    when (node.type) {
        "text" -> {
            val baseStyle = TextStyle(
                fontSize = (node.fontSize * textScale).sp,
                fontWeight = if (node.bold) FontWeight.Bold else FontWeight.Normal,
                textAlign = when (node.align) {
                    "center" -> TextAlign.Center
                    "right" -> TextAlign.Right
                    else -> TextAlign.Left
                },
            )
            val style = colorProvider(node.color)?.let { baseStyle.copy(color = it) } ?: baseStyle
            val link = node.linkUrl
            if (link != null) {
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse(link),
                ).addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                Text(
                    text = node.value ?: "",
                    style = style,
                    maxLines = 40,
                    modifier = GlanceModifier.clickable(
                        androidx.glance.appwidget.action.actionStartActivity(intent),
                    ),
                )
            } else {
                Text(
                    text = node.value ?: "",
                    style = style,
                    maxLines = 40,
                )
            }
        }
        "progress" -> {
            androidx.glance.appwidget.LinearProgressIndicator(
                progress = node.progressValue.coerceIn(0f, 1f),
                modifier = GlanceModifier.fillMaxWidth().height(10.dp),
                color = colorProvider(node.color) ?: ColorProvider(Color(Tokens.PINK_ARGB)),
                backgroundColor = colorProvider(node.backgroundColor) ?: ColorProvider(Color(Tokens.TRACK_GRAY_ARGB)),
            )
        }
        "canvas" -> {
            // Rendered directly (no remember): Glance content is rebuilt per update,
            // and unkeyed remember slots in child loops could mix bitmaps between canvases.
            val bmp = app.scriptlet.engine.CanvasRenderer.render(node.opsJson, node.width, node.height)
            if (bmp != null) {
                Image(
                    provider = ImageProvider(bmp),
                    contentDescription = null,
                    modifier = GlanceModifier.width(node.width.dp).height(node.height.dp),
                )
            } else {
                Spacer(GlanceModifier.width(node.width.dp).height(node.height.dp))
            }
        }
        "spacer" -> {
            // Flexible spacers (length < 0) are handled by the parent container,
            // which can give them weight; here only fixed lengths occur.
            if (parentHorizontal) Spacer(GlanceModifier.width(node.length.coerceAtLeast(0).dp))
            else Spacer(GlanceModifier.height(node.length.coerceAtLeast(0).dp))
        }
        "divider" -> {
            val cp = colorProvider(node.color) ?: ColorProvider(Color(Tokens.TRACK_GRAY_ARGB))
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(cp),
            ) {}
        }
        "image" -> {
            val bmp = node.imageSource?.let { images[it] }
            if (bmp != null) {
                var m: GlanceModifier = GlanceModifier
                if (node.width > 0) m = m.width(node.width.dp)
                if (node.height > 0) m = m.height(node.height.dp)
                Image(
                    provider = ImageProvider(bmp),
                    contentDescription = null,
                    modifier = m,
                )
            } else {
                Spacer(GlanceModifier.width((if (node.width > 0) node.width else 1).dp))
            }
        }
        else -> { // widget | stack : container
            var m: GlanceModifier = if (isRoot) GlanceModifier.fillMaxSize() else GlanceModifier
            val bg = colorProvider(node.backgroundColor)
            if (bg != null) {
                m = m.background(bg)
                if (node.cornerRadius > 0) m = m.cornerRadius(node.cornerRadius.dp)
            }
            if (!isRoot) {
                if (node.width > 0) m = m.width(node.width.dp)
                if (node.height > 0) m = m.height(node.height.dp)
            }
            m = m.padding(
                start = node.padding.getOrElse(1) { 0 }.dp,
                top = node.padding.getOrElse(0) { 0 }.dp,
                end = node.padding.getOrElse(3) { 0 }.dp,
                bottom = node.padding.getOrElse(2) { 0 }.dp,
            )
            if (node.type == "layer") {
                Box(modifier = m) {
                    node.children.forEach { child ->
                        RenderNode(child, parentHorizontal = false, images = images, textScale = textScale)
                    }
                }
                return
            }
            val horizontal = node.direction == "horizontal"
            if (isRoot) {
                Box(modifier = m) {
                    if (horizontal) {
                        Row(
                            verticalAlignment = when (node.alignItems) {
                                "center" -> Alignment.CenterVertically
                                "end" -> Alignment.Bottom
                                else -> Alignment.Top
                            },
                        ) { ChildrenH(node, images, textScale) }
                    } else {
                        Column(
                            horizontalAlignment = when (node.alignItems) {
                                "center" -> Alignment.CenterHorizontally
                                "end" -> Alignment.End
                                else -> Alignment.Start
                            },
                        ) { ChildrenV(node, images, textScale) }
                    }
                }
            } else if (horizontal) {
                Row(
                    modifier = m,
                    verticalAlignment = when (node.alignItems) {
                        "center" -> Alignment.CenterVertically
                        "end" -> Alignment.Bottom
                        else -> Alignment.Top
                    },
                ) { ChildrenH(node, images, textScale) }
            } else {
                Column(
                    modifier = m,
                    horizontalAlignment = when (node.alignItems) {
                        "center" -> Alignment.CenterHorizontally
                        "end" -> Alignment.End
                        else -> Alignment.Start
                    },
                ) { ChildrenV(node, images, textScale) }
            }
        }
    }
}

@Composable
private fun RowScope.ChildrenH(node: WidgetNode, images: Map<String, Bitmap>, textScale: Float = 1f) {
    node.children.forEachIndexed { i, child ->
        if (i > 0 && node.spacing > 0) Spacer(GlanceModifier.width(node.spacing.dp))
        if (child.type == "spacer" && child.length < 0) {
            Spacer(GlanceModifier.defaultWeight())
        } else {
            RenderNode(child, parentHorizontal = true, images = images, textScale = textScale)
        }
    }
}

@Composable
private fun ColumnScope.ChildrenV(node: WidgetNode, images: Map<String, Bitmap>, textScale: Float = 1f) {
    node.children.forEachIndexed { i, child ->
        if (i > 0 && node.spacing > 0) Spacer(GlanceModifier.height(node.spacing.dp))
        if (child.type == "spacer" && child.length < 0) {
            Spacer(GlanceModifier.defaultWeight())
        } else {
            RenderNode(child, parentHorizontal = false, images = images, textScale = textScale)
        }
    }
}
