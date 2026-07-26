package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val senderAvatarIcon: String = "person",
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromMe: Boolean = false,
    val messageType: String = "TEXT", // "TEXT", "VOICE", "FILE", "CODE", "IMAGE", "SYSTEM"
    val voiceDurationSeconds: Int = 0,
    val voiceTranscript: String? = null,
    val fileName: String? = null,
    val fileSize: String? = null,
    val fileUri: String? = null,
    val isEncrypted: Boolean = true,
    val status: String = "DELIVERED", // "SENDING", "SENT", "DELIVERED", "READ"
    val isStarred: Boolean = false
)
