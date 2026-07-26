package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Alex Rivera",
    val email: String = "alex.rivera@workai.studio",
    val phoneNumber: String = "+1 (555) 019-2834",
    val role: String = "VP of Product & AI Strategy",
    val department: String = "Product & AI Ops",
    val managementLevel: String = "Super Admin", // Super Admin, Admin, Manager, Senior Lead, Associate
    val isSuperAdmin: Boolean = true,
    val companyName: String = "WorkAI Global Tech",
    val statusBio: String = "🚀 Collaborating with AI Copilots at Work",
    val avatarIcon: String = "person_3",
    val isDarkMode: Boolean = true,
    val defaultWallpaper: String = "DOODLE_DARK",
    val bubbleStyle: String = "CLASSIC_EMERALD",
    val fontSizeSp: Int = 14,
    val isCloudSyncEnabled: Boolean = true,
    val lastSyncTime: Long = System.currentTimeMillis(),
    val isPinProtected: Boolean = false,
    val pinCode: String = "1234",
    val totalAiQueries: Int = 42,
    val totalVoiceNotes: Int = 15,
    val dataRetentionDays: Int = 90,
    val allowFileSharing: Boolean = true,
    val allowGroupCreation: Boolean = true,
    // Notification controls
    val notifyMessages: Boolean = true,
    val notifyGroups: Boolean = true,
    val notifyPreviewText: Boolean = true,
    val notificationTone: String = "Enterprise Emerald Tone",
    // Security setup
    val isBiometricEnabled: Boolean = false,
    val appLockTimeout: String = "Immediately",
    val screenSecurityEnabled: Boolean = false,
    val twoFactorEnabled: Boolean = true,
    // Knowledge Transfer (KT) & Handover
    val isKtHandoverActive: Boolean = false,
    val handoverSuccessorName: String = "Sarah Chen",
    val handoverSuccessorRole: String = "Director of Product Management",
    val handoverNotes: String = "Handing over active AI prompts, group admin rights, and sprint roadmap logs."
)

