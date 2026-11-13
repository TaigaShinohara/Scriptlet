package app.scriptlet.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Design tokens (see docs/ART_DIRECTION.md). One source for the values the
 * screens share, so a colour or a radius cannot drift between screens.
 * Meaning and art direction live in ART_DIRECTION.md; numbers live here.
 */
object Tokens {
    // Brand colours by role: pink = the hero action, orange = caution/next,
    // purple = grouping, sky = information. The theme schemes carry the
    // light/dark variants of these roles for Material components; these
    // constants are for direct drawing (previews, widget defaults, mocks).
    val Pink = Color(0xFFC2185B)
    val Orange = Color(0xFFE65100)
    val Purple = Color(0xFF6A1B9A)
    val Sky = Color(0xFF0277BD)

    /** Neutral grey used for progress tracks and unfilled areas. */
    val TrackGray = Color(0xFFE5E7EB)

    /** Dark slate of the widget mock cards (the "home screen" world). */
    val SlateDark = Color(0xFF111827)
    val SlateFrame = Color(0xFF1F2937)

    /** Sub text inside the onboarding phone mock (mock world only). */
    val MockTextSub = Color(0xFFCBD5E1)

    /** Code tint inside the onboarding phone mock (mock world only). */
    val MockSky = Color(0xFFA5F3FC)

    // ARGB twins for non-Compose rendering (Glance).
    const val PINK_ARGB: Long = 0xFFC2185B
    const val TRACK_GRAY_ARGB: Long = 0xFFE5E7EB

    // Shape and spacing: one radius for cards, one screen padding, an
    // 8/12/16 spacing ladder — no in-between values.
    val CardRadius = 16.dp
    val ScreenPadding = 16.dp
    val CardPadding = 16.dp
    val ItemSpacing = 12.dp
    val SmallSpacing = 8.dp
}
