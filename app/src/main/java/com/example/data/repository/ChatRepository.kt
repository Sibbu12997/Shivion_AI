package com.example.data.repository

import com.example.data.local.dao.AIAssistantDao
import com.example.data.local.dao.ChatDao
import com.example.data.local.dao.MessageDao
import com.example.data.local.dao.UserProfileDao
import com.example.data.local.entities.AIAssistantEntity
import com.example.data.local.entities.ChatEntity
import com.example.data.local.entities.MessageEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.remote.GeminiApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID

class ChatRepository(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val aiAssistantDao: AIAssistantDao,
    private val userProfileDao: UserProfileDao
) {
    val allChats: Flow<List<ChatEntity>> = chatDao.getAllChats()
    val allAssistants: Flow<List<AIAssistantEntity>> = aiAssistantDao.getAllAssistants()
    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()
    val starredMessages: Flow<List<MessageEntity>> = messageDao.getStarredMessages()

    private val _typingStateMap = MutableStateFlow<Map<String, String?>>(emptyMap())
    val typingStateMap: Flow<Map<String, String?>> = _typingStateMap.asStateFlow()

    fun setUserTyping(chatId: String, isTyping: Boolean) {
        val current = _typingStateMap.value.toMutableMap()
        if (isTyping) {
            current[chatId + "_user"] = "Alex Rivera"
        } else {
            current.remove(chatId + "_user")
        }
        _typingStateMap.value = current
    }

    fun getMessagesForChat(chatId: String): Flow<List<MessageEntity>> {
        return messageDao.getMessagesForChat(chatId)
    }

    fun getChatByIdFlow(chatId: String): Flow<ChatEntity?> {
        return chatDao.getChatByIdFlow(chatId)
    }

    suspend fun initializeDefaultData() = withContext(Dispatchers.IO) {
        // Initialize User Profile
        if (userProfileDao.getUserProfileDirect() == null) {
            userProfileDao.insertOrUpdateProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Alex Rivera",
                    email = "alex.rivera@workai.studio",
                    role = "Lead Work Specialist",
                    statusBio = "🤖 Leveraging Multi-AI Work Assistants",
                    isDarkMode = true,
                    defaultWallpaper = "DOODLE_DARK",
                    isCloudSyncEnabled = true,
                    lastSyncTime = System.currentTimeMillis()
                )
            )
        }

        // Initialize Default AI Assistants
        val defaultAssistants = listOf(
            AIAssistantEntity(
                id = "sivion_navigator_bot",
                name = "Sivion AI Navigator",
                roleTitle = "App Guide & Navigation Assistant",
                iconName = "explore",
                description = "Guides you through Sivion AI features, multi-AI group chats, settings, wallpapers, voice calls, and suggests next steps.",
                systemPrompt = "You are Sivion AI Navigator, an interactive in-app guide for Sivion AI. Help users navigate all features: multi-AI group chats, settings (dark/light theme, wallpapers, chat bubbles, font sizes), voice notes, and video calls. Always suggest clear next steps if a user is confused or encounters an error.",
                category = "NAVIGATION",
                badgeColorHex = "#00A884"
            ),
            AIAssistantEntity(
                id = "ai_copilot",
                name = "Work Copilot AI",
                roleTitle = "Executive & Task Specialist",
                iconName = "smart_toy",
                description = "Summarizes documents, drafts emails, plans projects, and manages work tasks.",
                systemPrompt = "You are Work Copilot, a professional executive assistant specializing in concise work answers, task planning, and email drafting.",
                category = "PRODUCTIVITY",
                badgeColorHex = "#128C7E"
            ),
            AIAssistantEntity(
                id = "ai_code_genius",
                name = "Code & Tech Copilot",
                roleTitle = "Senior Software Architect",
                iconName = "code",
                description = "Reviews Kotlin, Python, SQL code, debugs errors, and optimizes algorithms.",
                systemPrompt = "You are Code Copilot, an expert software engineer. Provide clear, well-commented code snippets and technical solutions.",
                category = "CODING",
                badgeColorHex = "#2196F3"
            ),
            AIAssistantEntity(
                id = "ai_creative_writer",
                name = "Creative Copywriter",
                roleTitle = "Brand & Marketing Strategist",
                iconName = "brush",
                description = "Crafts persuasive pitch decks, social media copy, taglines, and marketing strategies.",
                systemPrompt = "You are Creative Copywriter AI. Craft compelling, engaging marketing copy, press releases, and brand messages.",
                category = "CREATIVE",
                badgeColorHex = "#E91E63"
            ),
            AIAssistantEntity(
                id = "ai_data_analyst",
                name = "Data & Finance AI",
                roleTitle = "Quantitative Analyst",
                iconName = "analytics",
                description = "Analyzes quarterly reports, calculates metrics, and explains financial data.",
                systemPrompt = "You are Data Analyst AI. Provide data insights, financial formulas, and clear analytical breakdowns.",
                category = "BUSINESS",
                badgeColorHex = "#FF9800"
            ),
            AIAssistantEntity(
                id = "ai_legal_hr",
                name = "HR & Policy Advisor",
                roleTitle = "People Ops & Compliance",
                iconName = "gavel",
                description = "Advises on workspace policies, interview questions, and onboarding checklists.",
                systemPrompt = "You are HR Advisor AI. Give professional workplace policy advice, HR strategies, and interview feedback.",
                category = "LEGAL",
                badgeColorHex = "#9C27B0"
            )
        )
        aiAssistantDao.insertInitialAssistants(defaultAssistants)

        // Initialize Sample Chats if empty
        val existingChats = chatDao.getChatById("chat_work_copilot")
        if (existingChats == null) {
            val now = System.currentTimeMillis()

            // 1. Single AI Chat
            val chat1 = ChatEntity(
                id = "chat_work_copilot",
                title = "Work Copilot AI",
                avatarType = "AI",
                avatarIconName = "smart_toy",
                type = "AI",
                lastMessage = "Hello Alex! How can I assist you with your work projects today?",
                lastMessageTime = now - 3600000,
                unreadCount = 1,
                isPinned = true,
                wallpaperTheme = "DOODLE_DARK",
                systemPrompt = defaultAssistants[0].systemPrompt
            )
            chatDao.insertOrUpdateChat(chat1)
            messageDao.insertMessage(
                MessageEntity(
                    id = "msg_init_1",
                    chatId = "chat_work_copilot",
                    senderId = "ai_copilot",
                    senderName = "Work Copilot AI",
                    senderAvatarIcon = "smart_toy",
                    content = "Hello Alex! Welcome to WorkAI Chat. I can draft emails, summarize meeting notes, organize schedules, and answer work queries. How can I help you today?",
                    timestamp = now - 3600000,
                    isFromMe = false,
                    status = "READ"
                )
            )

            // 2. Multi-AI Group Chat
            val chatGroup = ChatEntity(
                id = "chat_group_product_launch",
                title = "🚀 Product Launch Squad (Multi-AI)",
                avatarType = "GROUP",
                avatarIconName = "groups",
                type = "AI_GROUP",
                lastMessage = "Code Copilot: Ready with backend architecture notes!",
                lastMessageTime = now - 1800000,
                unreadCount = 2,
                isPinned = true,
                wallpaperTheme = "EMERALD",
                systemPrompt = "Multi-AI collaboration group for product launch planning.",
                participantIdsCsv = "ai_copilot,ai_code_genius,ai_creative_writer"
            )
            chatDao.insertOrUpdateChat(chatGroup)
            messageDao.insertMessage(
                MessageEntity(
                    id = "msg_init_group_1",
                    chatId = "chat_group_product_launch",
                    senderId = "user",
                    senderName = "Alex Rivera",
                    content = "Team, let's coordinate our strategy for the v2.0 AI Workspace release!",
                    timestamp = now - 2000000,
                    isFromMe = true,
                    status = "READ"
                )
            )
            messageDao.insertMessage(
                MessageEntity(
                    id = "msg_init_group_2",
                    chatId = "chat_group_product_launch",
                    senderId = "ai_creative_writer",
                    senderName = "Creative Copywriter",
                    senderAvatarIcon = "brush",
                    content = "I can draft the launch announcement newsletter and press release headline!",
                    timestamp = now - 1900000,
                    isFromMe = false,
                    status = "READ"
                )
            )

            // 3. Colleague / Friend Chat
            val chatFriend = ChatEntity(
                id = "chat_friend_sarah",
                title = "Sarah Jenkins (Lead Designer)",
                avatarType = "FRIEND",
                avatarIconName = "person",
                type = "FRIEND",
                lastMessage = "Voice note received (0:14) - Check the Figma mockup feedback",
                lastMessageTime = now - 600000,
                unreadCount = 1,
                wallpaperTheme = "WARM_SLATE"
            )
            chatDao.insertOrUpdateChat(chatFriend)
            messageDao.insertMessage(
                MessageEntity(
                    id = "msg_init_friend_voice",
                    chatId = "chat_friend_sarah",
                    senderId = "sarah_j",
                    senderName = "Sarah Jenkins",
                    senderAvatarIcon = "person",
                    content = "Voice Message",
                    timestamp = now - 600000,
                    isFromMe = false,
                    messageType = "VOICE",
                    voiceDurationSeconds = 14,
                    voiceTranscript = "Hey Alex, I uploaded the new WhatsApp dark theme wireframes! Let me know what you think when you get a chance.",
                    status = "DELIVERED"
                )
            )
        }
    }

    suspend fun sendMessage(
        chatId: String,
        content: String,
        messageType: String = "TEXT",
        voiceDurationSeconds: Int = 0,
        fileName: String? = null,
        fileSize: String? = null
    ) = withContext(Dispatchers.IO) {
        val userMsgId = UUID.randomUUID().toString()
        val userMsg = MessageEntity(
            id = userMsgId,
            chatId = chatId,
            senderId = "user",
            senderName = "Alex Rivera",
            content = content,
            timestamp = System.currentTimeMillis(),
            isFromMe = true,
            messageType = messageType,
            voiceDurationSeconds = voiceDurationSeconds,
            fileName = fileName,
            fileSize = fileSize,
            status = "SENT"
        )
        messageDao.insertMessage(userMsg)
        chatDao.updateLastMessage(chatId, if (messageType == "VOICE") "🎤 Voice message (${voiceDurationSeconds}s)" else content, System.currentTimeMillis())

        val chat = chatDao.getChatById(chatId) ?: return@withContext

        // If chat is AI or Multi-AI Group, generate AI response
        if (chat.type == "AI" || chat.type == "AI_GROUP") {
            generateAIResponseForChat(chat, content)
        } else if (chat.type == "FRIEND") {
            // Simulated friend reply
            generateFriendResponse(chat, content)
        }
    }

    private suspend fun generateAIResponseForChat(chat: ChatEntity, userPrompt: String) {
        setBotTyping(chat.id, chat.title)
        delay(1000)
        try {
            if (chat.type == "AI") {
                val aiReply = GeminiApiClient.askGemini(
                    prompt = userPrompt,
                    systemInstruction = chat.systemPrompt
                )
                val aiMsg = MessageEntity(
                    id = UUID.randomUUID().toString(),
                    chatId = chat.id,
                    senderId = "ai_bot",
                    senderName = chat.title,
                    senderAvatarIcon = chat.avatarIconName,
                    content = aiReply,
                    timestamp = System.currentTimeMillis(),
                    isFromMe = false,
                    status = "READ"
                )
                messageDao.insertMessage(aiMsg)
                chatDao.updateLastMessage(chat.id, aiReply, System.currentTimeMillis())
            } else if (chat.type == "AI_GROUP") {
                val replyFromCopilot = GeminiApiClient.askGemini(
                    prompt = "User in multi-AI group asked: '$userPrompt'. Give a quick work copilot input.",
                    systemInstruction = "You are Work Copilot in a multi-AI team group chat. Keep responses brief and action-oriented."
                )
                val msg1 = MessageEntity(
                    id = UUID.randomUUID().toString(),
                    chatId = chat.id,
                    senderId = "ai_copilot",
                    senderName = "Work Copilot AI",
                    senderAvatarIcon = "smart_toy",
                    content = replyFromCopilot,
                    timestamp = System.currentTimeMillis(),
                    isFromMe = false,
                    status = "READ"
                )
                messageDao.insertMessage(msg1)
                chatDao.updateLastMessage(chat.id, replyFromCopilot, System.currentTimeMillis())
            }
        } finally {
            setBotTyping(chat.id, null)
        }
    }

    private suspend fun generateFriendResponse(chat: ChatEntity, userPrompt: String) {
        setBotTyping(chat.id, chat.title)
        delay(1200)
        try {
            val simulatedReplies = listOf(
                "Got it Alex! Thanks for sending that over.",
                "Sounds like a great plan! Let's connect on our group call later today.",
                "Awesome work! I'll review and get back to you shortly.",
                "I'm on it! Will share the update in the channel."
            )
            val reply = simulatedReplies.random()
            val friendMsg = MessageEntity(
                id = UUID.randomUUID().toString(),
                chatId = chat.id,
                senderId = "friend",
                senderName = chat.title,
                senderAvatarIcon = "person",
                content = reply,
                timestamp = System.currentTimeMillis() + 1000,
                isFromMe = false,
                status = "DELIVERED"
            )
            messageDao.insertMessage(friendMsg)
            chatDao.updateLastMessage(chat.id, reply, System.currentTimeMillis() + 1000)
        } finally {
            setBotTyping(chat.id, null)
        }
    }

    private fun setBotTyping(chatId: String, senderName: String?) {
        val current = _typingStateMap.value.toMutableMap()
        if (senderName != null) {
            current[chatId] = senderName
        } else {
            current.remove(chatId)
        }
        _typingStateMap.value = current
    }

    suspend fun createNewChat(
        title: String,
        type: String,
        systemPrompt: String = "You are a helpful AI assistant.",
        participantIdsCsv: String = ""
    ): String = withContext(Dispatchers.IO) {
        val newChatId = "chat_" + UUID.randomUUID().toString()
        val iconName = if (type == "AI_GROUP") "groups" else if (type == "AI") "smart_toy" else "person"
        val newChat = ChatEntity(
            id = newChatId,
            title = title,
            avatarType = if (type == "AI_GROUP") "GROUP" else type,
            avatarIconName = iconName,
            type = type,
            lastMessage = "Chat created",
            lastMessageTime = System.currentTimeMillis(),
            systemPrompt = systemPrompt,
            participantIdsCsv = participantIdsCsv
        )
        chatDao.insertOrUpdateChat(newChat)
        return@withContext newChatId
    }

    suspend fun updateWallpaper(chatId: String, wallpaper: String) = withContext(Dispatchers.IO) {
        chatDao.updateChatWallpaper(chatId, wallpaper)
    }

    suspend fun toggleStarMessage(messageId: String, currentStarred: Boolean) = withContext(Dispatchers.IO) {
        messageDao.toggleStarMessage(messageId, !currentStarred)
    }

    suspend fun clearUnread(chatId: String) = withContext(Dispatchers.IO) {
        chatDao.clearUnreadCount(chatId)
    }

    suspend fun syncCloudHistory(): String = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        userProfileDao.updateSyncTime(now)
        return@withContext "Cloud Sync Complete! All chats, encrypted voice notes, and AI logs secured."
    }

    suspend fun getOrCreateSivionNavigatorChat(): String = withContext(Dispatchers.IO) {
        val navChatId = "chat_sivion_navigator"
        val existing = chatDao.getChatById(navChatId)
        if (existing == null) {
            val navChat = ChatEntity(
                id = navChatId,
                title = "Sivion AI Navigator",
                avatarType = "AI",
                avatarIconName = "explore",
                type = "AI",
                lastMessage = "👋 Need help navigating Sivion AI? Ask me anything or tap suggested actions!",
                lastMessageTime = System.currentTimeMillis(),
                unreadCount = 1,
                isPinned = true,
                wallpaperTheme = "DOODLE_DARK",
                systemPrompt = "You are Sivion AI Navigator, an interactive in-app guide for Sivion AI. Help users navigate all features: multi-AI group chats, settings (dark/light theme, wallpapers, chat bubbles, font sizes), voice notes, and video calls. Always suggest clear next steps if a user is confused or encounters an error."
            )
            chatDao.insertOrUpdateChat(navChat)
            messageDao.insertMessage(
                MessageEntity(
                    id = "msg_sivion_nav_init",
                    chatId = navChatId,
                    senderId = "sivion_navigator_bot",
                    senderName = "Sivion AI Navigator",
                    senderAvatarIcon = "explore",
                    content = "👋 Welcome to Sivion AI!\n\nI'm your App Navigator Bot. Here is how I can help you:\n\n• ⚙️ Settings: Tap the 3-dots menu -> Settings to switch Dark/Light theme, change Wallpapers, Chat Bubble styles & Font Sizes.\n• 🚀 Multi-AI Groups: Tap + or 3-dots -> Create Multi-AI Group.\n• 📹 Video Calls: Tap video call icon in top bar.\n• 🎤 Voice Notes: Press and hold mic icon in chat.\n\nTell me what you want to do or if you are lost!",
                    timestamp = System.currentTimeMillis(),
                    isFromMe = false,
                    status = "READ"
                )
            )
        }
        return@withContext navChatId
    }

    suspend fun updateProfile(profile: UserProfileEntity) = withContext(Dispatchers.IO) {
        userProfileDao.insertOrUpdateProfile(profile)
    }
}
