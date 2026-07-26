package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.UserProfileEntity
import com.example.ui.theme.WhatsAppAccentBlue
import com.example.ui.theme.WhatsAppGreenLight
import com.example.ui.theme.WhatsAppGreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userProfile: UserProfileEntity,
    onBackClick: () -> Unit,
    onToggleTheme: (Boolean) -> Unit,
    onWallpaperSelect: (String) -> Unit,
    onBubbleStyleSelect: (String) -> Unit,
    onFontSizeSelect: (Int) -> Unit,
    onOpenNavigatorBot: () -> Unit,
    onOpenProfileEdit: () -> Unit,
    onSyncCloud: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var fontSliderValue by remember { mutableFloatStateOf(userProfile.fontSizeSp.toFloat()) }

    val isDark = userProfile.isDarkMode
    val bgColor = if (isDark) Color(0xFF0B141B) else Color(0xFFF0F2F5)
    val headerBg = if (isDark) Color(0xFF202C33) else Color(0xFF008069)
    val cardBg = if (isDark) Color(0xFF111B21) else Color.White
    val textPrimary = if (isDark) Color.White else Color(0xFF111B21)
    val textSecondary = if (isDark) Color(0xFF8696A0) else Color(0xFF667781)

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBg)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "Settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }
        },
        containerColor = bgColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Profile Summary Header Card
            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenProfileEdit() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = WhatsAppGreenPrimary,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userProfile.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = userProfile.role,
                            fontSize = 13.sp,
                            color = textSecondary
                        )
                        Text(
                            text = userProfile.statusBio,
                            fontSize = 12.sp,
                            color = WhatsAppGreenLight,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Text(
                        text = "Edit >",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WhatsAppGreenLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sivion AI Navigation Bot Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1F2C34) else Color(0xFFE7FCE3)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenNavigatorBot() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = WhatsAppGreenPrimary,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = "Sivion Navigator",
                            tint = Color.White,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🤖 Sivion AI Navigator Assistant",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = "Tap to open interactive bot guide for app navigation & troubleshooting.",
                            fontSize = 12.sp,
                            color = textSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 1: Theme & Display Settings
            SettingsSectionHeader(title = "Appearance & Theme", icon = Icons.Default.Palette, color = WhatsAppGreenLight)

            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Dark / Light Toggle Switch
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = WhatsAppGreenLight,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isDark) "Dark Theme Active" else "Light Theme Active",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary
                            )
                            Text(
                                text = "Switch between high-contrast dark and clean light modes",
                                fontSize = 12.sp,
                                color = textSecondary
                            )
                        }
                        Switch(
                            checked = isDark,
                            onCheckedChange = { onToggleTheme(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = WhatsAppGreenPrimary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 2: Wallpaper Settings
            SettingsSectionHeader(title = "Chat Wallpaper", icon = Icons.Default.Wallpaper, color = Color(0xFF2196F3))

            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Selected Theme Wallpaper: ${userProfile.defaultWallpaper}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val wallpapers = listOf(
                        "DOODLE_DARK" to "Doodle Dark",
                        "OLED_BLACK" to "Pitch Black",
                        "EMERALD" to "WhatsApp Emerald",
                        "WARM_SLATE" to "Warm Slate",
                        "CLEAN_LIGHT" to "Clean Light",
                        "SUBTLE_GRID" to "Subtle Grid"
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        wallpapers.chunked(2).forEach { row ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                row.forEach { (key, label) ->
                                    val isSelected = userProfile.defaultWallpaper == key
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) WhatsAppGreenPrimary else (if (isDark) Color(0xFF202C33) else Color(0xFFE9EDEF)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { onWallpaperSelect(key) }
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color.White else textPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 3: Chat Bubble Settings
            SettingsSectionHeader(title = "Chat Bubble Style", icon = Icons.Default.Brush, color = Color(0xFFE91E63))

            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Customize message bubble accent colors",
                        fontSize = 13.sp,
                        color = textSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val bubbleStyles = listOf(
                        "CLASSIC_EMERALD" to "Classic Emerald",
                        "MODERN_TEAL" to "Modern Teal",
                        "MINIMAL_GRAPHITE" to "Minimal Slate",
                        "VIBRANT_NEON" to "Vibrant Cyber",
                        "LAVENDER_SOFT" to "Soft Lavender"
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        bubbleStyles.forEach { (key, label) ->
                            val isSelected = userProfile.bubbleStyle == key
                            val badgeColor = when (key) {
                                "MODERN_TEAL" -> Color(0xFF00695C)
                                "MINIMAL_GRAPHITE" -> Color(0xFF37474F)
                                "VIBRANT_NEON" -> Color(0xFF1A237E)
                                "LAVENDER_SOFT" -> Color(0xFF4A148C)
                                else -> WhatsAppGreenPrimary
                            }

                            Surface(
                                shape = CircleShape,
                                color = badgeColor,
                                modifier = Modifier
                                    .size(42.dp)
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = if (isSelected) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { onBubbleStyleSelect(key) }
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.DoneAll,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Live Bubble Style Preview Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDark) Color(0xFF0B141B) else Color(0xFFE9EDEF),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Bubble Preview:", fontSize = 11.sp, color = textSecondary)
                            Spacer(modifier = Modifier.height(6.dp))

                            // Received Preview
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isDark) Color(0xFF202C33) else Color.White,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(end = 40.dp)
                            ) {
                                Text(
                                    text = "Hello Alex! Previewing chat bubble styling.",
                                    fontSize = userProfile.fontSizeSp.sp,
                                    color = textPrimary,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Sent Preview
                            val sentBg = when (userProfile.bubbleStyle) {
                                "MODERN_TEAL" -> Color(0xFF00695C)
                                "MINIMAL_GRAPHITE" -> Color(0xFF37474F)
                                "VIBRANT_NEON" -> Color(0xFF1A237E)
                                "LAVENDER_SOFT" -> Color(0xFF4A148C)
                                else -> if (isDark) Color(0xFF005C4B) else Color(0xFFD9FDD3)
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = sentBg,
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(start = 40.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(
                                        text = "Looks great! Status delivered.",
                                        fontSize = userProfile.fontSizeSp.sp,
                                        color = if (isDark || userProfile.bubbleStyle != "CLASSIC_EMERALD") Color.White else Color(0xFF111B21)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.DoneAll,
                                        contentDescription = "Read",
                                        tint = WhatsAppAccentBlue,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 4: Font Size Settings
            SettingsSectionHeader(title = "Font Size", icon = Icons.Default.FormatSize, color = Color(0xFFFF9800))

            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Current Size: ${userProfile.fontSizeSp} sp (${
                            when (userProfile.fontSizeSp) {
                                12 -> "Small"
                                14 -> "Medium (Default)"
                                16 -> "Large"
                                18 -> "Extra Large"
                                else -> "Custom"
                            }
                        })",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = fontSliderValue,
                        onValueChange = {
                            fontSliderValue = it
                        },
                        onValueChangeFinished = {
                            val newSize = fontSliderValue.toInt()
                            onFontSizeSelect(newSize)
                        },
                        valueRange = 12f..18f,
                        steps = 2,
                        colors = SliderDefaults.colors(
                            thumbColor = WhatsAppGreenPrimary,
                            activeTrackColor = WhatsAppGreenLight
                        )
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("12sp (Small)", fontSize = 11.sp, color = textSecondary)
                        Text("14sp (Medium)", fontSize = 11.sp, color = textSecondary)
                        Text("16sp (Large)", fontSize = 11.sp, color = textSecondary)
                        Text("18sp (X-Large)", fontSize = 11.sp, color = textSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 5: Cloud Sync & Security
            SettingsSectionHeader(title = "Sync & Security", icon = Icons.Default.Security, color = Color(0xFF9C27B0))

            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Cloud Sync Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSyncCloud()
                                Toast.makeText(context, "Encrypted cloud backup completed!", Toast.LENGTH_SHORT).show()
                            }
                    ) {
                        Icon(Icons.Default.CloudSync, contentDescription = null, tint = WhatsAppGreenLight, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Cloud Backup & Sync", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                            Text("Last sync: Just now • All chats encrypted", fontSize = 12.sp, color = textSecondary)
                        }
                        Icon(Icons.Default.CloudDone, contentDescription = null, tint = WhatsAppGreenLight)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // PIN Protection
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("App Security PIN Lock", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                            Text("Require 4-digit PIN to open Sivion AI", fontSize = 12.sp, color = textSecondary)
                        }
                        Switch(
                            checked = userProfile.isPinProtected,
                            onCheckedChange = {
                                Toast.makeText(context, "PIN Security toggled", Toast.LENGTH_SHORT).show()
                            },
                            colors = SwitchDefaults.colors(checkedTrackColor = WhatsAppGreenPrimary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String, icon: ImageVector, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
