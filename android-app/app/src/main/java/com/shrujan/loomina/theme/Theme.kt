package com.shrujan.loomina.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = InstaPurple,
    secondary = InstaMagenta,
    tertiary = InstaOrange,
    background = InstaDark,
    surface = InstaSurfaceDark,
    onPrimary = InstaOnDark,
    onSecondary = InstaOnDark,
    onTertiary = InstaOnDark,
    onBackground = InstaOnDark,
    onSurface = InstaOnDark
)

private val LightColorScheme = lightColorScheme(
    primary = InstaPurple,
    secondary = InstaMagenta,
    tertiary = InstaOrange,
    background = InstaLight,
    surface = InstaSurfaceLight,
    onPrimary = InstaOnLight,
    onSecondary = InstaOnLight,
    onTertiary = InstaOnLight,
    onBackground = InstaOnLight,
    onSurface = InstaOnLight
)

@Composable
fun LoominaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
