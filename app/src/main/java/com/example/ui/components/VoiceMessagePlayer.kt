package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppGreenLight

@Composable
fun VoiceMessagePlayer(
    durationSeconds: Int,
    transcript: String?,
    isFromMe: Boolean,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var showTranscript by remember { mutableStateOf(false) }

    val waveformBars = remember {
        listOf(0.3f, 0.6f, 0.2f, 0.8f, 0.4f, 0.9f, 0.5f, 0.7f, 0.3f, 0.8f, 0.6f, 0.4f, 0.9f, 0.2f, 0.5f, 0.7f)
    }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            // Play/Pause button
            Surface(
                shape = CircleShape,
                color = if (isFromMe) Color(0xFF00A884) else WhatsAppGreenLight,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { isPlaying = !isPlaying }
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause voice" else "Play voice",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Waveform & Timer
            Column(modifier = Modifier.weight(1f)) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                ) {
                    val barWidth = 4.dp.toPx()
                    val gap = 3.dp.toPx()
                    val totalBars = waveformBars.size
                    val activeColor = if (isFromMe) Color(0xFF25D366) else Color(0xFF00A884)
                    val inactiveColor = Color.Gray.copy(alpha = 0.5f)

                    waveformBars.forEachIndexed { index, heightFactor ->
                        val x = index * (barWidth + gap)
                        val barHeight = size.height * heightFactor
                        val startY = (size.height - barHeight) / 2
                        val endY = startY + barHeight

                        drawLine(
                            color = if (isPlaying && index < totalBars / 2) activeColor else inactiveColor,
                            start = Offset(x, startY),
                            end = Offset(x, endY),
                            strokeWidth = barWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isPlaying) "0:06 / 0:${if (durationSeconds < 10) "0$durationSeconds" else durationSeconds}" else "0:${if (durationSeconds < 10) "0$durationSeconds" else durationSeconds}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Encrypted",
                        tint = WhatsAppGreenLight,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = "Encrypted Voice",
                        fontSize = 10.sp,
                        color = WhatsAppGreenLight,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Speed Selector pill
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.clickable {
                    playbackSpeed = when (playbackSpeed) {
                        1.0f -> 1.5f
                        1.5f -> 2.0f
                        else -> 1.0f
                    }
                }
            ) {
                Text(
                    text = "${playbackSpeed}x",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Transcript Toggle Button if transcript available
            if (!transcript.isNullOrBlank()) {
                IconButton(
                    onClick = { showTranscript = !showTranscript },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Subtitles,
                        contentDescription = "AI Transcript",
                        tint = if (showTranscript) WhatsAppGreenLight else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Expandable AI Voice Transcript
        AnimatedVisibility(visible = showTranscript && !transcript.isNullOrBlank()) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "✨ AI Voice Transcript:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = WhatsAppGreenLight
                    )
                    Text(
                        text = transcript ?: "",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
