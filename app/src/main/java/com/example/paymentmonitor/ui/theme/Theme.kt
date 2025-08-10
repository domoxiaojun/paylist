package com.example.paymentmonitor.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = iOSBlue,
    onPrimary = Color.White,
    primaryContainer = iOSBlueLight,
    onPrimaryContainer = iOSBlueDark,
    secondary = iOSGreen,
    onSecondary = Color.White,
    secondaryContainer = iOSGreenLight,
    onSecondaryContainer = iOSGreenDark,
    tertiary = iOSOrange,
    onTertiary = Color.White,
    tertiaryContainer = iOSOrangeLight,
    onTertiaryContainer = iOSOrange,
    error = iOSRed,
    errorContainer = iOSRedLight,
    onError = Color.White,
    onErrorContainer = iOSRed,
    background = iOSBackground,
    onBackground = iOSTextPrimary,
    surface = Color.White,
    onSurface = iOSTextPrimary,
    surfaceVariant = iOSBackground,
    onSurfaceVariant = iOSTextSecondary,
    outline = iOSSeparator,
    outlineVariant = iOSTextTertiary,
)

private val DarkColorScheme = darkColorScheme(
    primary = iOSBlueLight,
    onPrimary = Color.White,
    primaryContainer = iOSBlueDark,
    onPrimaryContainer = iOSBlueLight,
    secondary = iOSGreenLight,
    onSecondary = Color.White,
    secondaryContainer = iOSGreenDark,
    onSecondaryContainer = iOSGreenLight,
    tertiary = iOSOrangeLight,
    onTertiary = Color.White,
    tertiaryContainer = iOSOrange,
    onTertiaryContainer = iOSOrangeLight,
    error = iOSRedLight,
    errorContainer = iOSRed,
    onError = Color.White,
    onErrorContainer = iOSRedLight,
    background = iOSDarkBackground,
    onBackground = iOSTextPrimaryDark,
    surface = iOSDarkSurface,
    onSurface = iOSTextPrimaryDark,
    surfaceVariant = iOSDarkSurface,
    onSurfaceVariant = iOSTextSecondaryDark,
    outline = iOSSeparatorDark,
    outlineVariant = iOSTextTertiaryDark,
)

@Composable
fun PaymentMonitorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}