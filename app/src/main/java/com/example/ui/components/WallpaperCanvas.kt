package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun WallpaperCanvas(
    wallpaperTheme: String = "DOODLE_DARK",
    modifier: Modifier = Modifier
) {
    val isLightBg = wallpaperTheme == "CLEAN_LIGHT" || wallpaperTheme == "SUBTLE_GRID"
    val bgColor = when (wallpaperTheme) {
        "OLED_BLACK" -> Color.Black
        "EMERALD" -> Color(0xFF061A14)
        "WARM_SLATE" -> Color(0xFF181C1E)
        "CLEAN_LIGHT" -> Color(0xFFE5DDD5)
        "SUBTLE_GRID" -> Color(0xFFEFEAE2)
        else -> Color(0xFF0B141B) // DOODLE_DARK default
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        if (wallpaperTheme != "OLED_BLACK") {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val lineAlpha = if (isLightBg) 0.12f else 0.07f
                val strokeWidth = 1.5.dp.toPx()
                val color = if (isLightBg) Color(0xFF111B21).copy(alpha = lineAlpha) else Color.White.copy(alpha = lineAlpha)

                val width = size.width
                val height = size.height

                if (wallpaperTheme == "SUBTLE_GRID") {
                    var x = 0f
                    while (x < width) {
                        drawLine(color, Offset(x, 0f), Offset(x, height), strokeWidth)
                        x += 60f
                    }
                    var y = 0f
                    while (y < height) {
                        drawLine(color, Offset(0f, y), Offset(width, y), strokeWidth)
                        y += 60f
                    }
                } else {
                    // Draw subtle doodle grid patterns across canvas
                    var y = 60f
                    while (y < height) {
                        var x = 60f
                        while (x < width) {
                            val patternType = ((x + y) / 100).toInt() % 4
                            when (patternType) {
                                0 -> { // Speech bubble
                                    val path = Path().apply {
                                        addRect(androidx.compose.ui.geometry.Rect(x, y, x + 24f, y + 18f))
                                    }
                                    drawPath(path, color, style = Stroke(strokeWidth))
                                }
                                1 -> { // Laptop / Monitor icon
                                    drawRect(color, Offset(x, y), androidx.compose.ui.geometry.Size(26f, 16f), style = Stroke(strokeWidth))
                                    drawLine(color, Offset(x - 4f, y + 18f), Offset(x + 30f, y + 18f), strokeWidth)
                                }
                                2 -> { // Sparkle star
                                    drawLine(color, Offset(x + 10f, y), Offset(x + 10f, y + 20f), strokeWidth)
                                    drawLine(color, Offset(x, y + 10f), Offset(x + 20f, y + 10f), strokeWidth)
                                }
                                3 -> { // Code brackets
                                    drawCircle(color, radius = 8f, center = Offset(x + 10f, y + 10f), style = Stroke(strokeWidth))
                                }
                            }
                            x += 160f
                        }
                        y += 180f
                    }
                }
            }
        }
    }
}
