package app.scriptlet.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Full brand schemes (docs/ART_DIRECTION.md): a quiet neutral base, pink as
// the single hero colour, orange/purple/sky only in their roles. Every role
// including containers is defined — leaving Material's baseline containers
// in place made selected cards glow stock-lavender, off-world for this app.
// Dark variants are the same roles in their brighter form.
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
)

@Composable
fun ScriptletTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkScheme else LightScheme,
        content = content,
    )
}
