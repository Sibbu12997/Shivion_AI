package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entities.AIAssistantEntity
import com.example.data.local.entities.ChatEntity
import com.example.data.local.entities.MessageEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.UUID

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.components.ToastData

enum class Screen {
    LOGIN,
    CHAT_LIST,
    CHAT_DETAIL,
    CREATE_GROUP,
    ADD_AI_BOT,
    GROUP_VIDEO_CALL,
    USER_PROFILE,
    SETTINGS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = ChatRepository(
        chatDao = db.chatDao(),
        messageDao = db.messageDao(),
        aiAssistantDao = db.aiAssistantDao(),
        userProfileDao = db.userProfileDao()
    )

    private val _currentScreen = MutableStateFlow(Screen.CHAT_LIST)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _currentChatId = MutableStateFlow<String?>(null)
    val currentChatId: StateFlow<String?> = _currentChatId.asStateFlow()

    private val _toastData = MutableStateFlow<ToastData?>(null)
    val toastData: StateFlow<ToastData?> = _toastData.asStateFlow()

    fun showToast(
        title: String,
        message: String? = null,
        icon: ImageVector = Icons.Default.CheckCircle,
        iconColor: Color = Color(0xFF00A884)
    ) {
        _toastData.value = ToastData(
            title = title,
            message = message,
            icon = icon,
            iconColor = iconColor
        )
    }

    fun dismissToast() {
        _toastData.value = null
    }

    val chats = repository.allChats
    val aiAssistants = repository.allAssistants
    val userProfile = repository.userProfile
    val starredMessages = repository.starredMessages
    val typingStateMap = repository.typingStateMap

    fun setUserTyping(isTyping: Boolean) {
        val chatId = _currentChatId.value ?: return
        repository.setUserTyping(chatId, isTyping)
    }

    val currentChatMessages = _currentChatId.flatMapLatest { chatId ->
        if (chatId == null) flowOf(emptyList())
        else repository.getMessagesForChat(chatId)
    }

    val currentChat = _currentChatId.flatMapLatest { chatId ->
        if (chatId == null) flowOf(null)
        else repository.getChatByIdFlow(chatId)
    }

    init {
        viewModelScope.launch {
            repository.initializeDefaultData()
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun openChat(chatId: String) {
        _currentChatId.value = chatId
        viewModelScope.launch {
            repository.clearUnread(chatId)
        }
        _currentScreen.value = Screen.CHAT_DETAIL
    }

    fun sendMessage(
        content: String,
        messageType: String = "TEXT",
        voiceDurationSeconds: Int = 0,
        fileName: String? = null,
        fileSize: String? = null
    ) {
        val chatId = _currentChatId.value ?: return
        viewModelScope.launch {
            repository.sendMessage(
                chatId = chatId,
                content = content,
                messageType = messageType,
                voiceDurationSeconds = voiceDurationSeconds,
                fileName = fileName,
                fileSize = fileSize
            )
            when (messageType) {
                "FILE", "CODE" -> showToast("File shared successfully", fileName ?: "Attachment sent", Icons.Default.AttachFile, Color(0xFF2196F3))
                "VOICE" -> showToast("Voice message sent", "${voiceDurationSeconds}s audio note delivered", Icons.Default.Mic, Color(0xFFE91E63))
                else -> showToast("Message delivered", "Delivered to Sivion AI", Icons.Default.Send, Color(0xFF00A884))
            }
        }
    }

    fun createMultiAIGroup(title: String, assistantIds: List<String>) {
        viewModelScope.launch {
            val newChatId = repository.createNewChat(
                title = title,
                type = "AI_GROUP",
                systemPrompt = "Multi-AI Team Group Chat",
                participantIdsCsv = assistantIds.joinToString(",")
            )
            showToast("Multi-AI Group Created", "Group '$title' is ready", Icons.Default.CheckCircle, Color(0xFF00A884))
            openChat(newChatId)
        }
    }

    fun addCustomAIBot(name: String, roleTitle: String, systemPrompt: String) {
        viewModelScope.launch {
            val botId = "custom_ai_" + UUID.randomUUID().toString().take(6)
            db.aiAssistantDao().insertAssistant(
                AIAssistantEntity(
                    id = botId,
                    name = name,
                    roleTitle = roleTitle,
                    iconName = "smart_toy",
                    description = systemPrompt.take(80),
                    systemPrompt = systemPrompt,
                    category = "CUSTOM",
                    badgeColorHex = "#25D366",
                    isCustom = true
                )
            )
            val newChatId = repository.createNewChat(
                title = name,
                type = "AI",
                systemPrompt = systemPrompt
            )
            showToast("AI Assistant Added", "Created custom assistant $name", Icons.Default.CheckCircle, Color(0xFF00A884))
            openChat(newChatId)
        }
    }

    fun updateWallpaper(wallpaperTheme: String) {
        val chatId = _currentChatId.value ?: return
        viewModelScope.launch {
            repository.updateWallpaper(chatId, wallpaperTheme)
            showToast("Wallpaper Updated", "Applied to current chat", Icons.Default.Wallpaper, Color(0xFF2196F3))
        }
    }

    fun toggleStarMessage(messageId: String, currentStarred: Boolean) {
        viewModelScope.launch {
            repository.toggleStarMessage(messageId, currentStarred)
            showToast(if (!currentStarred) "Message Starred" else "Star Removed", icon = Icons.Default.CheckCircle)
        }
    }

    fun syncCloudHistory() {
        viewModelScope.launch {
            repository.syncCloudHistory()
            showToast("Cloud Sync Completed", "All messages backed up securely", Icons.Default.CloudDone, Color(0xFF00A884))
        }
    }

    fun saveUserProfile(profile: UserProfileEntity) {
        viewModelScope.launch {
            repository.updateProfile(profile)
            showToast("Profile Saved", "User profile details updated", Icons.Default.CheckCircle, Color(0xFF00A884))
        }
    }

    fun openSivionNavigator() {
        viewModelScope.launch {
            val navChatId = repository.getOrCreateSivionNavigatorChat()
            openChat(navChatId)
        }
    }

    fun updateThemeMode(isDark: Boolean, currentProfile: UserProfileEntity) {
        viewModelScope.launch {
            repository.updateProfile(currentProfile.copy(isDarkMode = isDark))
            showToast(
                title = if (isDark) "Dark Theme Enabled" else "Light Theme Enabled",
                message = "Applied theme app-wide",
                icon = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                iconColor = Color(0xFFFF9800)
            )
        }
    }

    fun updateDefaultWallpaper(wallpaper: String, currentProfile: UserProfileEntity) {
        viewModelScope.launch {
            repository.updateProfile(currentProfile.copy(defaultWallpaper = wallpaper))
            showToast("Default Wallpaper Set", "Theme set to $wallpaper", Icons.Default.Wallpaper, Color(0xFF2196F3))
        }
    }

    fun updateBubbleStyle(style: String, currentProfile: UserProfileEntity) {
        viewModelScope.launch {
            repository.updateProfile(currentProfile.copy(bubbleStyle = style))
            showToast("Bubble Accent Changed", "Style set to $style", Icons.Default.Brush, Color(0xFFE91E63))
        }
    }

    fun updateFontSize(fontSizeSp: Int, currentProfile: UserProfileEntity) {
        viewModelScope.launch {
            repository.updateProfile(currentProfile.copy(fontSizeSp = fontSizeSp))
            showToast("Font Size Updated", "Chat font size set to ${fontSizeSp}sp", Icons.Default.FormatSize, Color(0xFFFF9800))
        }
    }
}
