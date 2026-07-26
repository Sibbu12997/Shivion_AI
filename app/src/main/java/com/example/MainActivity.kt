package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.entities.UserProfileEntity
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.screens.AddAIBotScreen
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.ChatListScreen
import com.example.ui.screens.CreateGroupScreen
import com.example.ui.screens.GroupVideoCallScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SignInScreen
import com.example.ui.screens.UserProfileScreen
import com.example.ui.theme.WorkAIChatTheme

import com.example.ui.components.CustomToastHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val userProfile by mainViewModel.userProfile.collectAsStateWithLifecycle(initialValue = null)
            val toastData by mainViewModel.toastData.collectAsStateWithLifecycle()

            WorkAIChatTheme(darkTheme = userProfile?.isDarkMode ?: true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize()) {
                        WorkAIChatApp(
                            viewModel = mainViewModel,
                            userProfile = userProfile ?: UserProfileEntity()
                        )

                        CustomToastHost(
                            toast = toastData,
                            onDismiss = { mainViewModel.dismissToast() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkAIChatApp(
    viewModel: MainViewModel,
    userProfile: UserProfileEntity
) {
    val screen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val chats by viewModel.chats.collectAsStateWithLifecycle(initialValue = emptyList())
    val aiAssistants by viewModel.aiAssistants.collectAsStateWithLifecycle(initialValue = emptyList())
    val currentChat by viewModel.currentChat.collectAsStateWithLifecycle(initialValue = null)
    val currentChatMessages by viewModel.currentChatMessages.collectAsStateWithLifecycle(initialValue = emptyList())
    val typingStateMap by viewModel.typingStateMap.collectAsStateWithLifecycle(initialValue = emptyMap())

    when (screen) {
        Screen.LOGIN -> {
            LoginScreen(
                onLoginSuccess = { name, role, phone, dept, level ->
                    viewModel.saveUserProfile(
                        userProfile.copy(
                            name = name,
                            role = role,
                            phoneNumber = phone,
                            department = dept,
                            managementLevel = level,
                            isSuperAdmin = (level == "Super Admin")
                        )
                    )
                    viewModel.navigateTo(Screen.CHAT_LIST)
                }
            )
        }

        Screen.CHAT_LIST -> {
            ChatListScreen(
                chats = chats,
                aiAssistants = aiAssistants,
                userProfile = userProfile,
                onChatSelect = { chatId -> viewModel.openChat(chatId) },
                onCreateGroupClick = { viewModel.navigateTo(Screen.CREATE_GROUP) },
                onAddAIBotClick = { viewModel.navigateTo(Screen.ADD_AI_BOT) },
                onProfileClick = { viewModel.navigateTo(Screen.USER_PROFILE) },
                onStartVideoCallClick = { viewModel.navigateTo(Screen.GROUP_VIDEO_CALL) },
                onLogoutClick = { viewModel.navigateTo(Screen.LOGIN) },
                onOpenSettingsClick = { viewModel.navigateTo(Screen.SETTINGS) },
                onOpenNavigatorClick = { viewModel.openSivionNavigator() }
            )
        }

        Screen.CHAT_DETAIL -> {
            currentChat?.let { chat ->
                ChatDetailScreen(
                    chat = chat,
                    messages = currentChatMessages,
                    userProfile = userProfile,
                    typingSenderName = typingStateMap[chat.id],
                    onUserTypingChanged = { isTyping -> viewModel.setUserTyping(isTyping) },
                    onBackClick = { viewModel.navigateTo(Screen.CHAT_LIST) },
                    onSendMessage = { content, messageType, duration, fileName, fileSize ->
                        viewModel.sendMessage(content, messageType, duration, fileName, fileSize)
                    },
                    onWallpaperChange = { wallpaper -> viewModel.updateWallpaper(wallpaper) },
                    onToggleStarMessage = { msgId, isStarred -> viewModel.toggleStarMessage(msgId, isStarred) },
                    onStartVideoCall = { viewModel.navigateTo(Screen.GROUP_VIDEO_CALL) },
                    onOpenSettings = { viewModel.navigateTo(Screen.SETTINGS) },
                    onOpenNavigatorBot = { viewModel.openSivionNavigator() }
                )
            } ?: run {
                viewModel.navigateTo(Screen.CHAT_LIST)
            }
        }

        Screen.CREATE_GROUP -> {
            CreateGroupScreen(
                assistants = aiAssistants,
                onBackClick = { viewModel.navigateTo(Screen.CHAT_LIST) },
                onCreateGroupSubmit = { title, assistantIds ->
                    viewModel.createMultiAIGroup(title, assistantIds)
                }
            )
        }

        Screen.ADD_AI_BOT -> {
            AddAIBotScreen(
                onBackClick = { viewModel.navigateTo(Screen.CHAT_LIST) },
                onAddBotSubmit = { name, role, prompt ->
                    viewModel.addCustomAIBot(name, role, prompt)
                }
            )
        }

        Screen.GROUP_VIDEO_CALL -> {
            GroupVideoCallScreen(
                onEndCall = { viewModel.navigateTo(Screen.CHAT_LIST) }
            )
        }

        Screen.USER_PROFILE -> {
            UserProfileScreen(
                profile = userProfile,
                onBackClick = { viewModel.navigateTo(Screen.CHAT_LIST) },
                onSaveProfile = { updated -> viewModel.saveUserProfile(updated) },
                onSyncNowClick = { viewModel.syncCloudHistory() },
                onOpenSignInScreen = { viewModel.navigateTo(Screen.SIGN_IN) }
            )
        }

        Screen.SETTINGS -> {
            SettingsScreen(
                userProfile = userProfile,
                onBackClick = { viewModel.navigateTo(Screen.CHAT_LIST) },
                onToggleTheme = { isDark -> viewModel.updateThemeMode(isDark, userProfile) },
                onWallpaperSelect = { wallpaper -> viewModel.updateDefaultWallpaper(wallpaper, userProfile) },
                onBubbleStyleSelect = { style -> viewModel.updateBubbleStyle(style, userProfile) },
                onFontSizeSelect = { sizeSp -> viewModel.updateFontSize(sizeSp, userProfile) },
                onOpenNavigatorBot = { viewModel.openSivionNavigator() },
                onOpenProfileEdit = { viewModel.navigateTo(Screen.USER_PROFILE) },
                onSyncCloud = { viewModel.syncCloudHistory() }
            )
        }

        Screen.SIGN_IN -> {
            SignInScreen(
                currentEmail = userProfile.phoneNumber,
                onBackClick = { viewModel.navigateTo(Screen.USER_PROFILE) },
                onSignInSuccess = { email ->
                    viewModel.saveUserProfile(userProfile.copy(isCloudSyncEnabled = true))
                    viewModel.syncCloudHistory()
                    viewModel.navigateTo(Screen.USER_PROFILE)
                }
            )
        }
    }
}
