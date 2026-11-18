package com.ronda.navlab.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    primaryContainer = PurpleUltraLight,
    onPrimaryContainer = PurpleDark,
    secondary = PurpleMedium,
    onSecondary = Color.White,
    background = ScreenBackgroundLight,
    onBackground = TextPrimaryDark,
    surface = CardSurfaceWhite,
    onSurface = TextPrimaryDark,
    surfaceVariant = PurpleUltraLight,
    onSurfaceVariant = TextSubtlePurple,
    outline = BorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PurpleLight,
    onPrimary = PurpleDark,
    primaryContainer = PurpleDark,
    onPrimaryContainer = PurpleUltraLight,
    secondary = PurpleMedium,
    onSecondary = Color.White,
    background = TextPrimaryDark,
    onBackground = Color.White,
    surface = PurpleDark,
    onSurface = Color.White,
    surfaceVariant = PurplePrimary,
    onSurfaceVariant = PurpleUltraLight,
    outline = BorderLight
)

@Composable
fun NavLabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
