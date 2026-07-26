package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppDarkBackground
import com.example.ui.theme.WhatsAppDarkCard
import com.example.ui.theme.WhatsAppGreenLight
import com.example.ui.theme.WhatsAppGreenPrimary
import kotlinx.coroutines.delay

data class VideoParticipant(
    val name: String,
    val role: String,
    val isAI: Boolean,
    val isSpeaking: Boolean = false,
    val color: Color
)

@Composable
fun GroupVideoCallScreen(
    onEndCall: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isVideoOff by remember { mutableStateOf(false) }
    var isScreenSharing by remember { mutableStateOf(false) }
    var activeSpeakerIndex by remember { mutableIntStateOf(0) }

    val participants = listOf(
        VideoParticipant("Work Copilot AI", "Executive Assistant", true, activeSpeakerIndex == 0, WhatsAppGreenPrimary),
        VideoParticipant("Code Copilot AI", "Senior Engineer", true, activeSpeakerIndex == 1, Color(0xFF2196F3)),
        VideoParticipant("Sarah Jenkins", "Lead Designer", false, activeSpeakerIndex == 2, Color(0xFFE91E63)),
        VideoParticipant("Alex Rivera (You)", "Product Specialist", false, activeSpeakerIndex == 3, Color(0xFF9C27B0))
    )

    val transcripts = listOf(
        "Work Copilot AI: I've updated the sprint roadmap and summarized today's action items for the team.",
        "Code Copilot AI: Kotlin Coroutines & Room Database sync modules are all compiled and verified.",
        "Sarah Jenkins: The dark theme color palette and voice waveform player UI look super smooth!",
        "Work Copilot AI: Excellent. I can auto-generate the meeting transcript for all group members."
    )

    // Cycle active speakers for realistic call feel
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            activeSpeakerIndex = (activeSpeakerIndex + 1) % participants.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhatsAppDarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Call Top Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF111B21))
                    .padding(16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = WhatsAppGreenLight,
                    modifier = Modifier.size(10.dp)
                ) {}

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "🚀 Product Launch Sync (4 Participants)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = WhatsAppGreenPrimary.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "HD 1080p Encrypted",
                        fontSize = 10.sp,
                        color = WhatsAppGreenLight,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Video Grid Tiles
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(participants.size) { index ->
                    val p = participants[index]
                    val isCurrentSpeaker = p.isSpeaking

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = WhatsAppDarkCard,
                        modifier = Modifier
                            .padding(6.dp)
                            .height(180.dp)
                            .border(
                                width = if (isCurrentSpeaker) 2.5.dp else 0.dp,
                                color = if (isCurrentSpeaker) WhatsAppGreenLight else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Avatar Icon or Video representation
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = p.color,
                                    modifier = Modifier.size(56.dp)
                                ) {
                                    Icon(
                                        imageVector = if (p.isAI) Icons.Default.SmartToy else Icons.Default.Person,
                                        contentDescription = p.name,
                                        tint = Color.White,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = p.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Text(
                                    text = p.role,
                                    fontSize = 11.sp,
                                    color = WhatsAppGreenLight
                                )
                            }

                            // Active Speaker Tag
                            if (isCurrentSpeaker) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = WhatsAppGreenPrimary,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("Speaking", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Real-time AI Transcription Ticker
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1F2C34),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Live Subtitles",
                        tint = WhatsAppGreenLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = transcripts[activeSpeakerIndex],
                        fontSize = 12.sp,
                        color = Color.White,
                        lineHeight = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Bottom Call Controls
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF111B21))
                    .padding(vertical = 16.dp)
            ) {
                // Mic button
                Surface(
                    shape = CircleShape,
                    color = if (isMuted) Color.Red else Color(0xFF202C33),
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { isMuted = !isMuted }
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Video button
                Surface(
                    shape = CircleShape,
                    color = if (isVideoOff) Color.Red else Color(0xFF202C33),
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { isVideoOff = !isVideoOff }
                ) {
                    Icon(
                        imageVector = if (isVideoOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = "Camera",
                        tint = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Screen Share
                Surface(
                    shape = CircleShape,
                    color = if (isScreenSharing) WhatsAppGreenPrimary else Color(0xFF202C33),
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { isScreenSharing = !isScreenSharing }
                ) {
                    Icon(
                        imageVector = Icons.Default.ScreenShare,
                        contentDescription = "Screen Share",
                        tint = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // End Call button
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFEA4335),
                    modifier = Modifier
                        .size(56.dp)
                        .clickable { onEndCall() }
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }
    }
}
