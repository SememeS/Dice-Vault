package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = SophisticatedDarkPrimary,
    onPrimary = SophisticatedDarkOnPrimary,
    primaryContainer = SophisticatedDarkPrimaryContainer,
    onPrimaryContainer = SophisticatedDarkOnPrimaryContainer,
    secondary = SophisticatedDarkPrimary,
    onSecondary = SophisticatedDarkOnPrimary,
    secondaryContainer = SophisticatedDarkSecondaryContainer,
    onSecondaryContainer = SophisticatedDarkOnPrimaryContainer,
    background = SophisticatedDarkBg,
    onBackground = SophisticatedDarkTextPrimary,
    surface = SophisticatedDarkSurface,
    onSurface = SophisticatedDarkTextPrimary,
    surfaceVariant = SophisticatedDarkSecondaryContainer,
    onSurfaceVariant = SophisticatedDarkTextSecondary,
    outline = SophisticatedDarkOutline,
    error = SophisticatedDarkError
  )

private val LightColorScheme = DarkColorScheme // Force sophisticated dark for consistent premium experience

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to true for dark mode
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false, // Disable system dynamics to preserve custom aesthetic
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
