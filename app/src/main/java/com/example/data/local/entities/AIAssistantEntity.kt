package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_assistants")
data class AIAssistantEntity(
    @PrimaryKey val id: String,
    val name: String,
    val roleTitle: String,
    val iconName: String,
    val description: String,
    val systemPrompt: String,
    val category: String, // "PRODUCTIVITY", "CODING", "CREATIVE", "BUSINESS", "LEGAL"
    val badgeColorHex: String = "#128C7E",
    val isCustom: Boolean = false
)
