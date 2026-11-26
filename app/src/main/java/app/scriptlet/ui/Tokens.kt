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

    /** Soft dusk plum of the widget mock cards (the "home screen" world). */
    val SlateDark = Color(0xFF2A2135)
    val SlateFrame = Color(0xFF3B2D47)

    /** Sub text inside the onboarding phone mock (mock world only). */
    val MockTextSub = Color(0xFFCBD5E1)

    /** Code tint inside the onboarding phone mock (mock world only). */
    val MockSky = Color(0xFFA5F3FC)

    /** Deep navy card inside the onboarding phone mock (mock world only). */
    val MockNavy = Color(0xFF16283F)

    // ARGB twins for non-Compose rendering (Glance).
    const val PINK_ARGB: Long = 0xFFC2185B
    const val TRACK_GRAY_ARGB: Long = 0xFFE5E7EB

    // Shape and spacing: one generous radius for cards (ART_DIRECTION v2:
    // round and soft everywhere), one screen padding, an 8/12/16 spacing
    // ladder — no in-between values.
    val CardRadius = 24.dp
    val ScreenPadding = 16.dp
    val CardPadding = 16.dp
    val ItemSpacing = 12.dp
    val SmallSpacing = 8.dp
}

/**
 * Material 3 motion tokens (official values, m3.material.io — easing and
 * duration token spec). Motion in this app explains state changes and
 * answers input only (ART_DIRECTION: no ambient animation outside the
 * onboarding). The emphasized two-segment path has no single-bezier
 * form, so transitions use the emphasized-decelerate / -accelerate
 * beziers and the standard set, per the official fallback guidance.
 */
object Motion {
    /** emphasized decelerate — elements entering the screen. */
    val EmphasizedDecelerate = androidx.compose.animation.core.CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
    /** emphasized accelerate — elements leaving the screen. */
    val EmphasizedAccelerate = androidx.compose.animation.core.CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
    /** standard — utility transitions that begin and end on screen. */
    val Standard = androidx.compose.animation.core.CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    /** standard decelerate — utility elements entering. */
    val StandardDecelerate = androidx.compose.animation.core.CubicBezierEasing(0.0f, 0.0f, 0.0f, 1.0f)
    /** standard accelerate — utility elements leaving. */
    val StandardAccelerate = androidx.compose.animation.core.CubicBezierEasing(0.3f, 0.0f, 1.0f, 1.0f)

    const val SHORT4 = 200
    const val MEDIUM1 = 250
    const val MEDIUM2 = 300
    const val MEDIUM4 = 400
}
