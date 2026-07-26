package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.WhatsAppGreenLight
import com.example.ui.theme.WhatsAppGreenPrimary

enum class KeyboardTab {
    EMOJI,
    GIF,
    STICKER
}

data class GifItem(val id: String, val title: String, val emoji: String, val badge: String)
data class StickerItem(val id: String, val label: String, val icon: String, val packName: String)

@Composable
fun WhatsAppEmojiKeyboard(
    isOpen: Boolean,
    isDarkMode: Boolean,
    onEmojiSelect: (String) -> Unit,
    onBackspace: () -> Unit,
    onGifSelect: (title: String, gifEmoji: String) -> Unit,
    onStickerSelect: (stickerName: String) -> Unit,
    onClose: () -> Unit
) {
    AnimatedVisibility(
        visible = isOpen,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        var selectedTab by remember { mutableStateOf(KeyboardTab.EMOJI) }
        var emojiCategoryIndex by remember { mutableIntStateOf(0) }
        var gifSearchQuery by remember { mutableStateOf("") }

        val bgContainer = if (isDarkMode) Color(0xFF1F2C34) else Color(0xFFF0F2F5)
        val tabBg = if (isDarkMode) Color(0xFF121B22) else Color(0xFFE9EDEF)
        val cardColor = if (isDarkMode) Color(0xFF2A3942) else Color.White
        val textColor = if (isDarkMode) Color.White else Color(0xFF111B21)
        val subTextColor = if (isDarkMode) Color(0xFF8696A0) else Color(0xFF667781)

        val emojiCategories = listOf(
            "😃" to listOf("😂", "❤️", "🤣", "👍", "🙏", "🔥", "😊", "😍", "🎉", "💯", "😎", "✨", "👏", "🙌", "🥳", "🤔", "🥺", "🥰", "😜", "🤩", "💩", "😭", "🤯", "🤗"),
            "🖐️" to listOf("👍", "👎", "👌", "✌️", "🤞", "🤘", "🤟", "👈", "👉", "👆", "👇", "🖐️", "✋", "✊", "👊", "🤛", "🤜", "👏", "🙌", "🤲", "🙏", "🤝", "💪", "✍️"),
            "❤️" to listOf("❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔", "❣️", "💕", "💞", "💓", "💗", "💖", "✨", "🔥", "💥", "💫", "💯", "💢", "💬", "💭"),
            "💼" to listOf("💼", "💻", "📱", "📊", "📈", "📉", "🖥️", "🖨️", "📁", "📂", "📜", "📄", "📑", "✏️", "✒️", "📝", "🔒", "🔓", "🔑", "🛡️", "⚙️", "🛠️", "💡", "📌"),
            "🤖" to listOf("🤖", "🚀", "🛸", "👾", "🎯", "🔮", "⚡", "🌟", "🌈", "☀️", "🌙", "🪐", "🧠", "🔋", "📡", "🏆", "🎖️", "🥇", "🥈", "🥉", "🏅", "🎗️", "🎟️", "🎫"),
            "☕" to listOf("☕", "🍵", "🧃", "🥤", "🍺", "🍕", "🍔", "🍟", "🥪", "🌮", "🍩", "🍰", "🍎", "🥑", "🍣", "🍿", "🍫", "🍬", "🍭", "🍇", "🍓", "🍉", "🍒", "🍑")
        )

        val sampleGifs = listOf(
            GifItem("g1", "Work Victory Launch", "🎉", "TRENDING"),
            GifItem("g2", "Coding Bug Squashed", "💻", "DEV"),
            GifItem("g3", "Mind Blown AI", "🤯", "GEMINI"),
            GifItem("g4", "Coffee Fuel Mode", "☕", "WORK"),
            GifItem("g5", "Deal Closed Success", "🤝", "BIZ"),
            GifItem("g6", "Thumbs Up Approved", "👍", "REACTION"),
            GifItem("g7", "Rocketing Growth", "🚀", "METRICS"),
            GifItem("g8", "Bravo Claps Team", "👏", "CHEER")
        )

        val sampleStickers = listOf(
            StickerItem("s1", "APPROVED ✅", "✅", "WorkAI Official"),
            StickerItem("s2", "BUG FIXED 🐛", "🛠️", "WorkAI Official"),
            StickerItem("s3", "LGTM! 👍", "👌", "Dev Squad"),
            StickerItem("s4", "ON IT! 🚀", "⚡", "WorkAI Official"),
            StickerItem("s5", "COFFEE FIRST ☕", "☕", "Work Life"),
            StickerItem("s6", "SIVION AI 🤖", "🤖", "AI Bots"),
            StickerItem("s7", "BRAINSTORM 🧠", "💡", "Productivity"),
            StickerItem("s8", "DEADLINE DONE 🎯", "🏁", "Productivity")
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(bgContainer)
        ) {
            // Tab Row (Emoji / GIF / Sticker)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tabBg)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TabPill(
                        label = "Emojis",
                        icon = "😀",
                        isSelected = selectedTab == KeyboardTab.EMOJI,
                        isDarkMode = isDarkMode,
                        onClick = { selectedTab = KeyboardTab.EMOJI }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TabPill(
                        label = "GIFs",
                        icon = "👾",
                        isSelected = selectedTab == KeyboardTab.GIF,
                        isDarkMode = isDarkMode,
                        onClick = { selectedTab = KeyboardTab.GIF }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TabPill(
                        label = "Stickers",
                        icon = "🎨",
                        isSelected = selectedTab == KeyboardTab.STICKER,
                        isDarkMode = isDarkMode,
                        onClick = { selectedTab = KeyboardTab.STICKER }
                    )
                }

                if (selectedTab == KeyboardTab.EMOJI) {
                    IconButton(
                        onClick = onBackspace,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "Backspace",
                            tint = subTextColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Tab Content Body
            when (selectedTab) {
                KeyboardTab.EMOJI -> {
                    Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        // Category Selector Row
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            emojiCategories.forEachIndexed { index, pair ->
                                Text(
                                    text = pair.first,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .clickable { emojiCategoryIndex = index }
                                        .background(
                                            if (emojiCategoryIndex == index) WhatsAppGreenPrimary.copy(alpha = 0.2f) else Color.Transparent,
                                            CircleShape
                                        )
                                        .padding(6.dp)
                                )
                            }
                        }

                        // Emoji Grid
                        val currentEmojis = emojiCategories.getOrNull(emojiCategoryIndex)?.second ?: emptyList()
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(8),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        ) {
                            items(currentEmojis) { emoji ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clickable { onEmojiSelect(emoji) }
                                ) {
                                    Text(text = emoji, fontSize = 22.sp)
                                }
                            }
                        }
                    }
                }

                KeyboardTab.GIF -> {
                    Column(modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                        ) {
                            OutlinedTextField(
                                value = gifSearchQuery,
                                onValueChange = { gifSearchQuery = it },
                                placeholder = { Text("Search GIFs on GIPHY...", fontSize = 12.sp, color = subTextColor) },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = WhatsAppGreenPrimary, modifier = Modifier.size(16.dp)) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WhatsAppGreenPrimary,
                                    unfocusedBorderColor = subTextColor.copy(alpha = 0.3f),
                                    focusedTextColor = textColor,
                                    unfocusedTextColor = textColor
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                            )
                        }

                        val filteredGifs = sampleGifs.filter {
                            gifSearchQuery.isBlank() || it.title.contains(gifSearchQuery, ignoreCase = true)
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().weight(1f)
                        ) {
                            items(filteredGifs) { gif ->
                                Card(
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = cardColor),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onGifSelect(gif.title, gif.emoji) }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(10.dp)
                                    ) {
                                        Text(gif.emoji, fontSize = 28.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(gif.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = textColor)
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = WhatsAppGreenPrimary.copy(alpha = 0.15f),
                                                modifier = Modifier.padding(top = 2.dp)
                                            ) {
                                                Text(
                                                    text = "GIF • ${gif.badge}",
                                                    fontSize = 9.sp,
                                                    color = WhatsAppGreenLight,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                KeyboardTab.STICKER -> {
                    Column(modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp)) {
                        Text(
                            text = "WorkAI Custom Expressive Stickers",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = subTextColor,
                            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().weight(1f)
                        ) {
                            items(sampleStickers) { sticker ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = cardColor),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onStickerSelect(sticker.label) }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(10.dp)
                                    ) {
                                        Text(sticker.icon, fontSize = 26.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(sticker.label, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = textColor)
                                            Text(sticker.packName, fontSize = 10.sp, color = subTextColor)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabPill(
    label: String,
    icon: String,
    isSelected: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) WhatsAppGreenPrimary else (if (isDarkMode) Color(0xFF202C33) else Color(0xFFD1D7DB)),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(icon, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else (if (isDarkMode) Color(0xFF8696A0) else Color(0xFF54656F))
            )
        }
    }
}
