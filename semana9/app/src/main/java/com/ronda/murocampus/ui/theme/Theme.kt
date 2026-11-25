package com.ronda.murocampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CampusNightMint,
    onPrimary = CampusNight,
    primaryContainer = Color(0xFF24534D),
    onPrimaryContainer = Color(0xFFC2F2E7),
    secondary = CampusNightCoral,
    onSecondary = Color(0xFF5C1D12),
    tertiary = CampusNightGold,
    background = CampusNight,
    onBackground = Color(0xFFE4EFEC),
    surface = CampusNightSurface,
    onSurface = Color(0xFFE4EFEC),
    surfaceVariant = Color(0xFF253332),
    onSurfaceVariant = Color(0xFFB8C9C5)
)

private val LightColorScheme = lightColorScheme(
    primary = CampusTeal,
    onPrimary = Color.White,
    primaryContainer = CampusMint,
    onPrimaryContainer = Color(0xFF063B35),
    secondary = CampusCoral,
    onSecondary = Color.White,
    tertiary = CampusGold,
    background = CampusPaper,
    onBackground = CampusInk,
    surface = Color.White,
    onSurface = CampusInk,
    surfaceVariant = Color(0xFFE7EFEC),
    onSurfaceVariant = Color(0xFF465652)
)

@Composable
fun MuroCampusTheme(
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
