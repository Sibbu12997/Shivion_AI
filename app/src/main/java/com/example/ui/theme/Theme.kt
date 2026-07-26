package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = WhatsAppGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = WhatsAppGreenTeal,
    onPrimaryContainer = Color.White,
    secondary = WhatsAppGreenLight,
    onSecondary = Color.Black,
    background = WhatsAppDarkBackground,
    surface = WhatsAppDarkSurface,
    surfaceVariant = WhatsAppDarkCard,
    onBackground = Color(0xFFE9EDEF),
    onSurface = Color(0xFFE9EDEF),
    onSurfaceVariant = Color(0xFF8696A0)
)

private val LightColorScheme = lightColorScheme(
    primary = WhatsAppLightHeader,
    onPrimary = Color.White,
    primaryContainer = WhatsAppGreenPrimary,
    onPrimaryContainer = Color.White,
    secondary = WhatsAppGreenTeal,
    onSecondary = Color.White,
    background = WhatsAppLightBackground,
    surface = WhatsAppLightSurface,
    surfaceVariant = Color(0xFFF0F2F5),
    onBackground = Color(0xFF111B21),
    onSurface = Color(0xFF111B21),
    onSurfaceVariant = Color(0xFF667781)
)

@Composable
fun WorkAIChatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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

