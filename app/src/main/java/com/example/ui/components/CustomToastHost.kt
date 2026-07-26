package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

data class ToastData(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val message: String? = null,
    val icon: ImageVector = Icons.Default.CheckCircle,
    val iconColor: Color = Color(0xFF00A884),
    val durationMs: Long = 3000L
)

@Composable
fun CustomToastHost(
    toast: ToastData?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(toast?.id) {
        if (toast != null) {
            delay(toast.durationMs)
            onDismiss()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(100f)
            .padding(horizontal = 16.dp, vertical = 40.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = toast != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
        ) {
            toast?.let { data ->
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1F2C34),
                    shadowElevation = 8.dp,
                    tonalElevation = 6.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .clickable { onDismiss() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = data.iconColor.copy(alpha = 0.18f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(
                                imageVector = data.icon,
                                contentDescription = null,
                                tint = data.iconColor,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = data.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            val subMsg = data.message
                            if (!subMsg.isNullOrBlank()) {
                                Text(
                                    text = subMsg,
                                    fontSize = 12.sp,
                                    color = Color(0xFF8696A0),
                                    modifier = Modifier.padding(top = 1.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF8696A0),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun String?.isNull_Blank(): Boolean = this == null || this.isBlank()
