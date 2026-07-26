package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,
    val title: String,
    val avatarType: String = "AI", // "AI", "FRIEND", "GROUP"
    val avatarIconName: String = "smart_toy",
    val type: String = "AI", // "AI", "FRIEND", "AI_GROUP"
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val wallpaperTheme: String = "DOODLE_DARK", // "DOODLE_DARK", "OLED_BLACK", "EMERALD", "WARM_SLATE"
    val systemPrompt: String = "You are a helpful AI work assistant.",
    val participantIdsCsv: String = "", // Comma-separated list of AI or user IDs in a group
    val tagsCsv: String = "Work"
)
