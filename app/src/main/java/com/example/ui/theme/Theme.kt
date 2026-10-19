package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WarmIvoryCream = Color(0xFFF4F1E8)
private val DeepEmeraldGreen = Color(0xFF123D32)
private val SoftGold = Color(0xFFF4D58A)

private val KaomiColorScheme = lightColorScheme(
    primary = DeepEmeraldGreen,
    onPrimary = SoftGold,
    primaryContainer = DeepEmeraldGreen,
    onPrimaryContainer = WarmIvoryCream,
    secondary = SoftGold,
    onSecondary = DeepEmeraldGreen,
    background = WarmIvoryCream,
    onBackground = DeepEmeraldGreen,
    surface = WarmIvoryCream,
    onSurface = DeepEmeraldGreen,
    surfaceVariant = WarmIvoryCream,
    onSurfaceVariant = DeepEmeraldGreen,
    surfaceTint = DeepEmeraldGreen,
    surfaceBright = WarmIvoryCream,
    surfaceDim = WarmIvoryCream,
    surfaceContainer = WarmIvoryCream,
    surfaceContainerHigh = WarmIvoryCream,
    surfaceContainerHighest = WarmIvoryCream,
    surfaceContainerLow = WarmIvoryCream,
    surfaceContainerLowest = WarmIvoryCream,
    outline = SoftGold,
    error = ErrorRed,
    onError = TextOnRed
)

private val NightDeepGreenBg = Color(0xFF0A1F1A)
private val NightDarkTeal = Color(0xFF0E2E27)

private val KaomiDarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = SoftGold,
    onPrimary = DeepEmeraldGreen,
    primaryContainer = IslamicNavySurface,
    onPrimaryContainer = IslamicIvory,
    secondary = SoftGold,
    onSecondary = DeepEmeraldGreen,
    background = NightDeepGreenBg,
    onBackground = IslamicIvory,
    surface = IslamicNavySurface,
    onSurface = IslamicIvory,
    surfaceVariant = NightDarkTeal,
    onSurfaceVariant = IslamicIvoryMuted,
    outline = SoftGold
)

@Composable
fun KaomiTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) KaomiDarkColorScheme else KaomiColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
