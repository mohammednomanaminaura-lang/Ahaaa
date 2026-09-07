package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MonarchColorScheme = darkColorScheme(
    primary = ElectricNeonBlue,
    onPrimary = VoidBlack,
    primaryContainer = VoidSurfaceVariant,
    onPrimaryContainer = ElectricNeonBlue,
    secondary = NecromancerPurple,
    onSecondary = TextLight,
    secondaryContainer = VoidSurface,
    onSecondaryContainer = NecromancerPurple,
    tertiary = CrimsonPenaltyRed,
    onTertiary = TextLight,
    background = VoidBlack,
    onBackground = TextLight,
    surface = VoidDark,
    onSurface = TextLight,
    surfaceVariant = VoidSurface,
    onSurfaceVariant = TextMuted,
    outline = VoidBorder,
    error = CrimsonPenaltyRed,
    onError = TextLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MonarchColorScheme,
        typography = Typography,
        content = content
    )
}
