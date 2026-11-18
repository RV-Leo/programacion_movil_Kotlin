package com.ronda.navlab.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Purple Brand Colors
val PurpleDark = Color(0xFF3F2B68)
val PurplePrimary = Color(0xFF654EA3)
val PurpleMedium = Color(0xFF7E66B8)
val PurpleLight = Color(0xFF9F88DB)
val PurpleUltraLight = Color(0xFFEFE8FB)

// Background & Surface
val BackgroundGradientTop = Color(0xFF4C367A)
val BackgroundGradientMiddle = Color(0xFF7A62B3)
val BackgroundGradientBottom = Color(0xFFEDE6FA)
val ScreenBackgroundLight = Color(0xFFF7F4FD)
val CardSurfaceWhite = Color(0xFFFFFFFF)

// Accent & Status Colors
val TextPrimaryDark = Color(0xFF22163B)
val TextSecondaryGray = Color(0xFF6E6A7D)
val TextSubtlePurple = Color(0xFF5A4D78)
val BorderLight = Color(0xFFE4DCF5)

val LogoutRed = Color(0xFFC62828)
val LogoutBgRed = Color(0xFFFDE8E8)

// Gradients
val AppHeaderGradient = Brush.verticalGradient(
    colors = listOf(
        BackgroundGradientTop,
        BackgroundGradientMiddle
    )
)

val AppBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        BackgroundGradientTop,
        BackgroundGradientMiddle,
        BackgroundGradientBottom
    )
)

val DetailBannerGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3A2563),
        Color(0xFF674FA3),
        Color(0xFF886EBF)
    )
)
