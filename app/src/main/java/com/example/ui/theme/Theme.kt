package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VaultDarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = DeepIndigo,
    onPrimaryContainer = Color.White,
    secondary = CyberPurple,
    onSecondary = Color.White,
    secondaryContainer = CyberPurpleDark,
    onSecondaryContainer = Color.White,
    tertiary = GoldenYellow,
    onTertiary = Color.Black,
    background = VaultBackground,
    onBackground = TextPrimary,
    surface = VaultSurface,
    onSurface = TextPrimary,
    surfaceVariant = VaultSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = VaultCardBorder,
    error = CrimsonDanger,
    onError = Color.White
)

@Composable
fun VaultHideTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = VaultDarkColorScheme,
        typography = Typography,
        content = content
    )
}
