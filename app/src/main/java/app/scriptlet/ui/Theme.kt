package app.scriptlet.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Brand schemes (docs/ART_DIRECTION.md v2: beautiful, cute, approachable —
// never rugged). The base is a warm white with soft pink-tinted surfaces in
// light mode, and a soft plum (never hard black/slate) in dark mode. Pink
// stays the single hero colour; orange/purple/sky keep their roles. Every
// role including background/surface is defined — leaving Material's stock
// surfaces in place read as a cold engineer dashboard, off-world here.
//
// M3 audit (2026-11-19, v0.19.0): the FULL role set is now defined. The
// error family, outline, the surfaceContainer ladder and the inverse roles
// previously fell back to stock Material values (grey outline, baseline
// red), which clashed with the brand world wherever they surfaced.
private val LightScheme = lightColorScheme(
    primary = Color(0xFFC2185B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFCE7F1),
    onPrimaryContainer = Color(0xFF880E4F),
    secondary = Color(0xFFE65100),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFEDD9),
    onSecondaryContainer = Color(0xFF7A2E00),
    tertiary = Color(0xFF6A1B9A),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF1E6F7),
    onTertiaryContainer = Color(0xFF4A1168),
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFFDECEA),
    onErrorContainer = Color(0xFF7A1F1A),
    background = Color(0xFFFFFBF7),
    onBackground = Color(0xFF3D2C34),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF3D2C34),
    surfaceVariant = Color(0xFFF9EDF2),
    onSurfaceVariant = Color(0xFF6B5560),
    surfaceDim = Color(0xFFEAD9E0),
    surfaceBright = Color(0xFFFFFBF7),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFFF6F9),
    surfaceContainer = Color(0xFFFBEFF4),
    surfaceContainerHigh = Color(0xFFF7E4EC),
    surfaceContainerHighest = Color(0xFFF2D9E4),
    outline = Color(0xFFA98A97),
    outlineVariant = Color(0xFFEDD9E2),
    inverseSurface = Color(0xFF3D2C34),
    inverseOnSurface = Color(0xFFFFEDF3),
    inversePrimary = Color(0xFFF78FB8),
)

private val DarkScheme = darkColorScheme(
    primary = Color(0xFFF78FB8),
    onPrimary = Color(0xFF4A0E24),
    primaryContainer = Color(0xFF3D2230),
    onPrimaryContainer = Color(0xFFFBD3E3),
    secondary = Color(0xFFFFAB7A),
    onSecondary = Color(0xFF4A2408),
    secondaryContainer = Color(0xFF402A1C),
    onSecondaryContainer = Color(0xFFFFDCC3),
    tertiary = Color(0xFFCE9BE8),
    onTertiary = Color(0xFF33154D),
    tertiaryContainer = Color(0xFF2F2140),
    onTertiaryContainer = Color(0xFFECD9F7),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF5F1410),
    errorContainer = Color(0xFF4A2320),
    onErrorContainer = Color(0xFFFFDAD4),
    background = Color(0xFF211722),
    onBackground = Color(0xFFF3E6EE),
    surface = Color(0xFF2C2130),
    onSurface = Color(0xFFF3E6EE),
    surfaceVariant = Color(0xFF3A2C40),
    onSurfaceVariant = Color(0xFFD8C4D2),
    surfaceDim = Color(0xFF211722),
    surfaceBright = Color(0xFF3A2C40),
    surfaceContainerLowest = Color(0xFF1A121C),
    surfaceContainerLow = Color(0xFF261B29),
    surfaceContainer = Color(0xFF2C2130),
    surfaceContainerHigh = Color(0xFF372A3D),
    surfaceContainerHighest = Color(0xFF42344B),
    outline = Color(0xFF7A6474),
    outlineVariant = Color(0xFF4A3A52),
    inverseSurface = Color(0xFFF3E6EE),
    inverseOnSurface = Color(0xFF3D2C34),
    inversePrimary = Color(0xFFC2185B),
)

// One rounding ladder for every component (buttons, chips, fields, cards),
// so nothing on screen keeps a hard corner.
private val SoftShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp),
)

@Composable
fun ScriptletTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkScheme else LightScheme,
        shapes = SoftShapes,
        content = content,
    )
}
