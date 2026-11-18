package com.ronda.tecsup_fit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = TecsupGreenLight,
    secondary = TecsupGreenPrimary,
    primaryContainer = TecsupGreenDark,
)

private val LightColorScheme = lightColorScheme(
    primary = TecsupGreenPrimary,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = TecsupGreenLight,
    onPrimaryContainer = TecsupGreenPrimary,
    surface = CardBgColor,
)

@Composable
fun TecsupFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
