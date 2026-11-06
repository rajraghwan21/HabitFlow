package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = ActionPink,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF831843),
    onSecondaryContainer = Color(0xFFFCE7F3),
    tertiary = AccentOrange,
    onTertiary = Color(0xFF182235),
    tertiaryContainer = Color(0xFF78350F),
    onTertiaryContainer = Color(0xFFFEF3C7),
    background = DarkBackground,
    onBackground = DarkPrimaryText,
    surface = DarkCardBackground,
    onSurface = DarkPrimaryText,
    surfaceVariant = DarkCardElevated,
    onSurfaceVariant = DarkSecondaryText,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0EFFF),
    onPrimaryContainer = Color(0xFF00387A),
    secondary = ActionPink,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0ED),
    onSecondaryContainer = Color(0xFF5A0022),
    tertiary = AccentOrange,
    onTertiary = Color(0xFF182235),
    tertiaryContainer = Color(0xFFFFF4D1),
    onTertiaryContainer = Color(0xFF593E00),
    background = LightBackground,
    onBackground = LightPrimaryText,
    surface = LightCardBackground,
    onSurface = LightPrimaryText,
    surfaceVariant = LightCardElevated,
    onSurfaceVariant = LightSecondaryText,
    outline = LightBorder
)

@Composable
fun HabitFlowTheme(
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
