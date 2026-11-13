package app.scriptlet.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.scriptlet.engine.ImageResolver
import app.scriptlet.engine.WidgetNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun parseColor(hex: String?): Color {
    if (hex.isNullOrBlank()) return Color.Unspecified
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (_: Exception) {
        Color.Unspecified
    }
}

@Composable
fun WidgetPreview(node: WidgetNode, modifier: Modifier = Modifier) {
    RenderNode(node, modifier, parentHorizontal = false)
}

@Composable
private fun RenderNode(node: WidgetNode, modifier: Modifier, parentHorizontal: Boolean) {
    when (node.type) {
        "text" -> {
            val color = parseColor(node.color)
            val context = androidx.compose.ui.platform.LocalContext.current
            var textModifier = modifier
            val link = node.linkUrl
            if (link != null) {
                textModifier = textModifier.clickable {
                    try {
                        context.startActivity(
                            android.content.Intent(
                                android.content.Intent.ACTION_VIEW,
                                android.net.Uri.parse(link),
                            ).addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK),
                        )
                    } catch (_: Exception) {
                    }
                }
            }
            Text(
                text = node.value ?: "",
                fontSize = node.fontSize.sp,
                fontWeight = if (node.bold) FontWeight.Bold else FontWeight.Normal,
                color = if (color == Color.Unspecified) {
                    if (link != null) Tokens.Sky else Color.Unspecified
                } else {
                    color
                },
                textAlign = when (node.align) {
                    "center" -> TextAlign.Center
                    "right" -> TextAlign.Right
                    else -> TextAlign.Left
                },
                modifier = textModifier,
            )
        }
        "progress" -> {
            val c = parseColor(node.color)
            val bg = parseColor(node.backgroundColor)
            androidx.compose.material3.LinearProgressIndicator(
                progress = { node.progressValue.coerceIn(0f, 1f) },
                modifier = modifier.fillMaxWidth().height(10.dp),
                color = if (c == Color.Unspecified) Tokens.Pink else c,
                trackColor = if (bg == Color.Unspecified) Tokens.TrackGray else bg,
            )
        }
        "canvas" -> {
            var bmp by remember(node.opsJson, node.width, node.height) { mutableStateOf<Bitmap?>(null) }
            androidx.compose.runtime.LaunchedEffect(node.opsJson, node.width, node.height) {
                bmp = withContext(Dispatchers.IO) {
                    app.scriptlet.engine.CanvasRenderer.render(node.opsJson, node.width, node.height)
                }
            }
            val b = bmp
            if (b != null) {
                Image(
                    bitmap = b.asImageBitmap(),
                    contentDescription = null,
                    modifier = modifier.width(node.width.dp).height(node.height.dp),
                )
            } else {
                Spacer(modifier.width(node.width.dp).height(node.height.dp))
            }
        }
        "layer" -> {
            val bg = parseColor(node.backgroundColor)
            val shape = RoundedCornerShape(node.cornerRadius.dp)
            var m = modifier
            if (bg != Color.Unspecified) m = m.background(bg, shape)
            if (node.width > 0) m = m.width(node.width.dp)
            if (node.height > 0) m = m.height(node.height.dp)
            m = m.padding(
                start = node.padding.getOrElse(1) { 0 }.dp,
                top = node.padding.getOrElse(0) { 0 }.dp,
                end = node.padding.getOrElse(3) { 0 }.dp,
                bottom = node.padding.getOrElse(2) { 0 }.dp,
            )
            Box(modifier = m) {
                node.children.forEach { child ->
                    RenderNode(child, Modifier, parentHorizontal = false)
                }
            }
        }
        "spacer" -> {
            if (parentHorizontal) Spacer(modifier.width(node.length.coerceAtLeast(0).dp))
            else Spacer(modifier.height(node.length.coerceAtLeast(0).dp))
        }
        "divider" -> {
            val c = parseColor(node.color)
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(if (c == Color.Unspecified) Tokens.TrackGray else c),
            )
        }
        "image" -> {
            var bmp by remember(node.imageSource, node.imageKind) { mutableStateOf<Bitmap?>(null) }
            androidx.compose.runtime.LaunchedEffect(node.imageSource, node.imageKind) {
                bmp = withContext(Dispatchers.IO) {
                    val src = node.imageSource
                    when {
                        src.isNullOrBlank() -> null
                        node.imageKind == "base64" -> ImageResolver.decodeBase64(src)
                        else -> ImageResolver.fetchUrl(src)
                    }
                }
            }
            val b = bmp
            if (b != null) {
                var m = modifier
                if (node.width > 0) m = m.width(node.width.dp)
                if (node.height > 0) m = m.height(node.height.dp)
                Image(bitmap = b.asImageBitmap(), contentDescription = null, modifier = m)
            } else {
                Spacer(modifier.width((if (node.width > 0) node.width else 1).dp))
            }
        }
        else -> { // widget | stack : container
            val bg = parseColor(node.backgroundColor)
            val shape = RoundedCornerShape(node.cornerRadius.dp)
            var m = modifier
            if (bg != Color.Unspecified) m = m.background(bg, shape)
            if (node.width > 0) m = m.width(node.width.dp)
            if (node.height > 0) m = m.height(node.height.dp)
            m = m.padding(
                start = node.padding.getOrElse(1) { 0 }.dp,
                top = node.padding.getOrElse(0) { 0 }.dp,
                end = node.padding.getOrElse(3) { 0 }.dp,
                bottom = node.padding.getOrElse(2) { 0 }.dp,
            )
            if (node.direction == "horizontal") {
                Row(
                    modifier = m,
                    horizontalArrangement = Arrangement.spacedBy(node.spacing.dp),
                    verticalAlignment = when (node.alignItems) {
                        "center" -> Alignment.CenterVertically
                        "end" -> Alignment.Bottom
                        else -> Alignment.Top
                    },
                ) {
                    node.children.forEach { child ->
                        if (child.type == "spacer" && child.length < 0) {
                            Spacer(Modifier.weight(1f))
                        } else {
                            RenderNode(child, Modifier, parentHorizontal = true)
                        }
                    }
                }
            } else {
                Column(
                    modifier = m,
                    verticalArrangement = Arrangement.spacedBy(node.spacing.dp),
                    horizontalAlignment = when (node.alignItems) {
                        "center" -> Alignment.CenterHorizontally
                        "end" -> Alignment.End
                        else -> Alignment.Start
                    },
                ) {
                    node.children.forEach { child ->
                        if (child.type == "spacer" && child.length < 0) {
                            Spacer(Modifier.weight(1f))
                        } else {
                            RenderNode(child, Modifier, parentHorizontal = false)
                        }
                    }
                }
            }
        }
    }
}
