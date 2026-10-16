package com.example.lab09_puntosadicionales.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Forest,
    onPrimary = Color.White,
    secondary = Lagoon,
    onSecondary = ForestDark,
    tertiary = Coral,
    background = Paper,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = LagoonLight,
    onSurfaceVariant = MutedInk,
    outline = Color(0xFFB3CCC2),
)

private val DarkColors = darkColorScheme(
    primary = LagoonDark,
    onPrimary = ForestDark,
    secondary = Mango,
    onSecondary = ForestDark,
    tertiary = Coral,
    background = Color(0xFF102622),
    onBackground = Color(0xFFF0FAF3),
    surface = Color(0xFF1A3630),
    onSurface = Color(0xFFF0FAF3),
    surfaceVariant = Color(0xFF2A4A42),
    onSurfaceVariant = Color(0xFFC4DCD0),
    outline = Color(0xFF5C8174),
)

@Composable
fun AmphibiansTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content,
    )
}
