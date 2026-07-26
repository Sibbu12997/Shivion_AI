package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppGreenPrimary

data class QuickWidgetAction(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun QuickAccessWidgetBar(
    onActionClick: (QuickWidgetAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val actions = listOf(
        QuickWidgetAction("ask_copilot", "Ask AI Copilot", "Quick work Q&A", Icons.Default.AutoAwesome, WhatsAppGreenPrimary),
        QuickWidgetAction("draft_email", "Draft Email", "Professional copy", Icons.Default.Email, Color(0xFFE91E63)),
        QuickWidgetAction("code_review", "Code Review", "Debug & optimize", Icons.Default.Code, Color(0xFF2196F3)),
        QuickWidgetAction("data_summary", "Analyze Data", "Financial metrics", Icons.Default.Analytics, Color(0xFFFF9800)),
        QuickWidgetAction("ai_video_call", "AI Group Call", "Start Video Sync", Icons.Default.VideoCall, Color(0xFF9C27B0))
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "⚡ Quick AI Work Widgets",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(actions) { action ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = action.color.copy(alpha = 0.12f),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { onActionClick(action) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = action.color,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = action.title,
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = action.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = action.subtitle,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
