package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import com.example.ui.components.TypingIndicatorBubble
import com.example.ui.components.WhatsAppEmojiKeyboard
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.ChatEntity
import com.example.data.local.entities.MessageEntity
import com.example.ui.components.FileAttachmentCard
import com.example.ui.components.VoiceMessagePlayer
import com.example.ui.components.WallpaperCanvas
import com.example.ui.theme.WhatsAppAccentBlue
import com.example.ui.theme.WhatsAppDarkCard
import com.example.ui.theme.WhatsAppDarkHeader
import com.example.ui.theme.WhatsAppGreenLight
import com.example.ui.theme.WhatsAppGreenPrimary
import com.example.ui.theme.WhatsAppGreenTeal
import com.example.ui.theme.WhatsAppLightHeader
import com.example.ui.theme.WhatsAppReceivedBubbleDark
import com.example.ui.theme.WhatsAppReceivedBubbleLight
import com.example.ui.theme.WhatsAppSentBubbleDark
import com.example.ui.theme.WhatsAppSentBubbleLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import com.example.data.local.entities.UserProfileEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chat: ChatEntity,
    messages: List<MessageEntity>,
    userProfile: UserProfileEntity = UserProfileEntity(),
    typingSenderName: String? = null,
    onUserTypingChanged: (Boolean) -> Unit = {},
    onBackClick: () -> Unit,
    onSendMessage: (content: String, messageType: String, voiceDuration: Int, fileName: String?, fileSize: String?) -> Unit,
    onWallpaperChange: (String) -> Unit,
    onToggleStarMessage: (String, Boolean) -> Unit,
    onStartVideoCall: () -> Unit,
    onOpenSettings: () -> Unit = {},
    onOpenNavigatorBot: () -> Unit = {}
) {
    var inputText by remember { mutableStateOf("") }
    var isEmojiKeyboardOpen by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isWallpaperMenuExpanded by remember { mutableStateOf(false) }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var voiceRecordDuration by remember { mutableIntStateOf(0) }
    var showAttachmentSheet by remember { mutableStateOf(false) }
    var codeSnippetModalText by remember { mutableStateOf("") }
    var isCodeModalOpen by remember { mutableStateOf(false) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showAddMemberModal by remember { mutableStateOf(false) }
    var showUpdateGroupIconModal by remember { mutableStateOf(false) }
    var currentGroupTitle by remember(chat.title) { mutableStateOf(chat.title) }

    val listState = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Scroll to bottom when new messages arrive or typing indicator appears
    LaunchedEffect(messages.size, typingSenderName) {
        val count = messages.size + (if (typingSenderName != null) 1 else 0)
        if (count > 0) {
            listState.animateScrollToItem(count - 1)
        }
    }

    // Voice record timer effect
    LaunchedEffect(isRecordingVoice) {
        if (isRecordingVoice) {
            voiceRecordDuration = 0
            while (isRecordingVoice) {
                delay(1000)
                voiceRecordDuration += 1
            }
        }
    }

    val icon = when (chat.avatarIconName) {
        "smart_toy" -> Icons.Default.SmartToy
        "code" -> Icons.Default.Code
        "brush" -> Icons.Default.Brush
        "analytics" -> Icons.Default.Analytics
        "gavel" -> Icons.Default.Gavel
        "groups" -> Icons.Default.Groups
        else -> Icons.Default.Person
    }

    val filteredMessages = remember(messages, searchQuery) {
        if (searchQuery.isBlank()) messages
        else messages.filter { it.content.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (userProfile.isDarkMode) WhatsAppDarkHeader else WhatsAppLightHeader)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                IconButton(onClick = {
                    if (isSearchActive) {
                        isSearchActive = false
                        searchQuery = ""
                    } else {
                        onBackClick()
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                if (isSearchActive) {
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 15.sp),
                        cursorBrush = SolidColor(WhatsAppGreenLight),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text("Search messages in chat...", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                            }
                            innerTextField()
                        }
                    )
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear Search", tint = Color.White)
                    }
                } else {
                    Surface(
                        shape = CircleShape,
                        color = WhatsAppGreenPrimary,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.padding(8.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = chat.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        val statusText = when {
                            typingSenderName != null -> "$typingSenderName is typing..."
                            inputText.isNotBlank() -> "Alex Rivera is typing..."
                            chat.type == "AI_GROUP" -> "Multi-AI Collaboration • Active"
                            else -> "Online • Gemini 3.5 AI Active"
                        }
                        Text(
                            text = statusText,
                            fontSize = 11.sp,
                            color = if (statusText.contains("typing")) Color(0xFF25D366) else WhatsAppGreenLight,
                            fontWeight = if (statusText.contains("typing")) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    IconButton(onClick = { isSearchActive = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Search Messages", tint = Color.White)
                    }

                    IconButton(onClick = onStartVideoCall) {
                        Icon(Icons.Default.VideoCall, contentDescription = "Video Call", tint = Color.White)
                    }

                    IconButton(onClick = { Toast.makeText(context, "Voice call initiating...", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.Call, contentDescription = "Voice Call", tint = Color.White)
                    }

                Box {
                    IconButton(onClick = { isMenuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("👥 Add Members to Group") },
                            onClick = {
                                isMenuExpanded = false
                                showAddMemberModal = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("🎨 Update Group Icon & Title") },
                            onClick = {
                                isMenuExpanded = false
                                showUpdateGroupIconModal = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("🖼️ Change Wallpaper") },
                            onClick = {
                                isMenuExpanded = false
                                isWallpaperMenuExpanded = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("📹 Start Group Video Call") },
                            onClick = {
                                isMenuExpanded = false
                                onStartVideoCall()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("🤖 Ask Sivion AI Navigator") },
                            onClick = {
                                isMenuExpanded = false
                                onOpenNavigatorBot()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("⚙️ Settings") },
                            onClick = {
                                isMenuExpanded = false
                                onOpenSettings()
                            }
                        )
                    }

                    DropdownMenu(
                        expanded = isWallpaperMenuExpanded,
                        onDismissRequest = { isWallpaperMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Doodle Dark (Default)") },
                            onClick = { isWallpaperMenuExpanded = false; onWallpaperChange("DOODLE_DARK") }
                        )
                        DropdownMenuItem(
                            text = { Text("Pitch OLED Black") },
                            onClick = { isWallpaperMenuExpanded = false; onWallpaperChange("OLED_BLACK") }
                        )
                        DropdownMenuItem(
                            text = { Text("WhatsApp Dark Emerald") },
                            onClick = { isWallpaperMenuExpanded = false; onWallpaperChange("EMERALD") }
                        )
                        DropdownMenuItem(
                            text = { Text("Warm Slate Charcoal") },
                            onClick = { isWallpaperMenuExpanded = false; onWallpaperChange("WARM_SLATE") }
                        )
                    }
                }
            }
        }
    },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (userProfile.isDarkMode) WhatsAppDarkHeader else Color(0xFFF0F2F5))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                // Quick AI Action Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val suggestions = listOf(
                        "✨ Summarize Thread",
                        "📝 Draft Status Email",
                        "📊 Analyze Key Metrics",
                        "🤖 Ask Copilot",
                        "💡 Brainstorm Ideas"
                    )
                    suggestions.forEach { chipText ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (userProfile.isDarkMode) Color(0xFF202C33) else Color(0xFFE9EDEF),
                            modifier = Modifier.clickable {
                                val prompt = chipText.substringAfter(" ")
                                inputText = prompt
                                onUserTypingChanged(true)
                            }
                        ) {
                            Text(
                                text = chipText,
                                fontSize = 11.sp,
                                color = WhatsAppGreenLight,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                if (isRecordingVoice) {
                    // Voice Recording Active Bar
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Surface(shape = CircleShape, color = Color.Red, modifier = Modifier.size(12.dp)) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Recording Encrypted Voice Note... 0:${if (voiceRecordDuration < 10) "0$voiceRecordDuration" else voiceRecordDuration}",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            isRecordingVoice = false
                            onSendMessage(
                                "Voice Message",
                                "VOICE",
                                if (voiceRecordDuration < 2) 5 else voiceRecordDuration,
                                null,
                                null
                            )
                        }) {
                            Icon(Icons.Default.Send, contentDescription = "Send Voice", tint = WhatsAppGreenLight)
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                    ) {
                        // Left Pill Box for Input
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = if (userProfile.isDarkMode) Color(0xFF202C33) else Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        isEmojiKeyboardOpen = !isEmojiKeyboardOpen
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isEmojiKeyboardOpen) Icons.Default.Keyboard else Icons.Outlined.EmojiEmotions,
                                        contentDescription = "Emojis",
                                        tint = if (isEmojiKeyboardOpen) WhatsAppGreenPrimary else Color(0xFF8696A0),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 4.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (inputText.isEmpty()) {
                                        Text(
                                            text = "Message",
                                            color = Color(0xFF8696A0),
                                            fontSize = 15.sp
                                        )
                                    }
                                    BasicTextField(
                                        value = inputText,
                                        onValueChange = {
                                            inputText = it
                                            onUserTypingChanged(it.isNotBlank())
                                        },
                                        textStyle = androidx.compose.ui.text.TextStyle(
                                            color = if (userProfile.isDarkMode) Color.White else Color(0xFF111B21),
                                            fontSize = 15.sp
                                        ),
                                        cursorBrush = SolidColor(WhatsAppGreenLight),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                IconButton(
                                    onClick = { showAttachmentSheet = true },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.AttachFile,
                                        contentDescription = "Attach",
                                        tint = Color(0xFF8696A0),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        onSendMessage("Photo attachment", "FILE", 0, "IMG_PHOTO_01.jpg", "1.8 MB")
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.PhotoCamera,
                                        contentDescription = "Camera",
                                        tint = Color(0xFF8696A0),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }

                        // Right Circular FAB (Mic / Send)
                        Surface(
                            shape = CircleShape,
                            color = WhatsAppGreenPrimary,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    if (inputText.isBlank()) {
                                        isRecordingVoice = true
                                    } else {
                                        val msg = inputText.trim()
                                        inputText = ""
                                        isEmojiKeyboardOpen = false
                                        onSendMessage(msg, "TEXT", 0, null, null)
                                    }
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                if (inputText.isBlank()) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "Voice Note",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Send",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // WhatsApp Style Emoji / GIF / Sticker Keyboard Drawer
                WhatsAppEmojiKeyboard(
                    isOpen = isEmojiKeyboardOpen,
                    isDarkMode = userProfile.isDarkMode,
                    onEmojiSelect = { emoji ->
                        inputText += emoji
                    },
                    onBackspace = {
                        if (inputText.isNotEmpty()) {
                            inputText = inputText.dropLast(1)
                        }
                    },
                    onGifSelect = { title, emoji ->
                        onSendMessage("GIF: $title $emoji", "TEXT", 0, null, null)
                        isEmojiKeyboardOpen = false
                    },
                    onStickerSelect = { stickerName ->
                        onSendMessage("Sticker: $stickerName", "TEXT", 0, null, null)
                        isEmojiKeyboardOpen = false
                    },
                    onClose = { isEmojiKeyboardOpen = false }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Active Wallpaper Background
            val activeWallpaper = if (chat.wallpaperTheme.isNotBlank() && chat.wallpaperTheme != "DOODLE_DARK") {
                chat.wallpaperTheme
            } else if (!userProfile.isDarkMode) {
                if (userProfile.defaultWallpaper == "DOODLE_DARK") "CLEAN_LIGHT" else userProfile.defaultWallpaper
            } else {
                userProfile.defaultWallpaper
            }
            WallpaperCanvas(wallpaperTheme = activeWallpaper)

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                items(filteredMessages, key = { it.id }) { message ->
                    MessageBubble(
                        message = message,
                        userProfile = userProfile,
                        onCopyText = {
                            clipboardManager.setText(AnnotatedString(message.content))
                            Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        onStarToggle = { onToggleStarMessage(message.id, message.isStarred) },
                        onSummarizeFile = {
                            onSendMessage("Summarize file content: ${message.content}", "TEXT", 0, null, null)
                        }
                    )
                }

                if (typingSenderName != null) {
                    item(key = "typing_indicator") {
                        TypingIndicatorBubble(
                            typingSenderName = typingSenderName,
                            isDarkMode = userProfile.isDarkMode
                        )
                    }
                }
            }
        }

        // Killer WhatsApp Style Attachment Sheet Modal
        if (showAttachmentSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAttachmentSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = WhatsAppDarkHeader
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Attach File or Share Workspace Asset", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(18.dp))

                    // Row 1: Document, Camera, Gallery, Audio, Location
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    ) {
                        AttachmentOptionItem(
                            title = "Document",
                            icon = Icons.Default.InsertDriveFile,
                            color = Color(0xFF7F66FF),
                            onClick = {
                                showAttachmentSheet = false
                                onSendMessage("Project_Requirements_v2.pdf", "FILE", 0, "Project_Requirements_v2.pdf", "2.4 MB")
                            }
                        )
                        AttachmentOptionItem(
                            title = "Camera",
                            icon = Icons.Default.CameraAlt,
                            color = Color(0xFFEC407A),
                            onClick = {
                                showAttachmentSheet = false
                                onSendMessage("Photo_Snap_2026.jpg", "IMAGE", 0, "Photo_Snap_2026.jpg", "1.8 MB")
                            }
                        )
                        AttachmentOptionItem(
                            title = "Gallery",
                            icon = Icons.Default.PhotoLibrary,
                            color = Color(0xFFAB47BC),
                            onClick = {
                                showAttachmentSheet = false
                                onSendMessage("Architecture_Diagram.png", "IMAGE", 0, "Architecture_Diagram.png", "3.2 MB")
                            }
                        )
                        AttachmentOptionItem(
                            title = "Audio",
                            icon = Icons.Default.Audiotrack,
                            color = Color(0xFFFF7043),
                            onClick = {
                                showAttachmentSheet = false
                                isRecordingVoice = true
                            }
                        )
                        AttachmentOptionItem(
                            title = "Location",
                            icon = Icons.Default.LocationOn,
                            color = Color(0xFF26A69A),
                            onClick = {
                                showAttachmentSheet = false
                                onSendMessage("📍 Headquarters (37.7749° N, 122.4194° W)", "TEXT", 0, null, null)
                            }
                        )
                    }

                    // Row 2: Contact, Poll, Code, AI Canvas, Workspace Zip
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AttachmentOptionItem(
                            title = "Contact",
                            icon = Icons.Default.ContactPhone,
                            color = Color(0xFF00ACC1),
                            onClick = {
                                showAttachmentSheet = false
                                onSendMessage("👤 Sarah Chen (Director of Product) - +1 555-019-2834", "TEXT", 0, null, null)
                            }
                        )
                        AttachmentOptionItem(
                            title = "Poll",
                            icon = Icons.Default.Poll,
                            color = Color(0xFF00897B),
                            onClick = {
                                showAttachmentSheet = false
                                onSendMessage("📊 Team Poll: Should we target Q3 release for Gemini 3.5 AI integration? Options: [1] Yes, Q3 [2] No, Q4", "TEXT", 0, null, null)
                            }
                        )
                        AttachmentOptionItem(
                            title = "Code",
                            icon = Icons.Default.Code,
                            color = Color(0xFF3F51B5),
                            onClick = {
                                showAttachmentSheet = false
                                isCodeModalOpen = true
                            }
                        )
                        AttachmentOptionItem(
                            title = "AI Canvas",
                            icon = Icons.Default.AutoAwesome,
                            color = Color(0xFFFFB300),
                            onClick = {
                                showAttachmentSheet = false
                                inputText = "🎨 Generate UI diagram for mobile dashboard with Jetpack Compose..."
                            }
                        )
                        AttachmentOptionItem(
                            title = "Zip File",
                            icon = Icons.Default.AttachFile,
                            color = Color(0xFF5C6BC0),
                            onClick = {
                                showAttachmentSheet = false
                                onSendMessage("WorkAI_Android_Source.zip", "FILE", 0, "WorkAI_Android_Source.zip", "14.2 MB")
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        // Add Member Modal Dialog
        if (showAddMemberModal) {
            var selectedMemberName by remember { mutableStateOf("Sarah Chen") }
            val colleagueList = listOf("Sarah Chen (Director of Product)", "David Miller (Software Architect)", "Priya Sharma (Lead AI Scientist)", "Marcus Vance (VP Operations)", "Elena Rostova (UX Lead)")

            AlertDialog(
                onDismissRequest = { showAddMemberModal = false },
                title = { Text("Add Colleague to Group Chat", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                text = {
                    Column {
                        Text("Select an office colleague to add to ${chat.title}:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(10.dp))

                        colleagueList.forEach { colleague ->
                            val isSelected = selectedMemberName == colleague
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) WhatsAppGreenPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { selectedMemberName = colleague }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(10.dp)
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = if (isSelected) WhatsAppGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(colleague, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showAddMemberModal = false
                            onSendMessage("➕ Added $selectedMemberName to the group conversation.", "TEXT", 0, null, null)
                            Toast.makeText(context, "$selectedMemberName added to group!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenPrimary)
                    ) {
                        Text("Add Member", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddMemberModal = false }) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
        }

        // Update Group Icon & Name Modal Dialog
        if (showUpdateGroupIconModal) {
            var newTitle by remember { mutableStateOf(currentGroupTitle) }

            AlertDialog(
                onDismissRequest = { showUpdateGroupIconModal = false },
                title = { Text("Update Group Name & Icon", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                text = {
                    Column {
                        Text("Edit the group name and icon branding:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Group Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Select Group Icon Branding:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            listOf(
                                Icons.Default.Groups,
                                Icons.Default.RocketLaunch,
                                Icons.Default.Code,
                                Icons.Default.Analytics,
                                Icons.Default.Psychology
                            ).forEach { iconVector ->
                                Surface(
                                    shape = CircleShape,
                                    color = WhatsAppGreenPrimary,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clickable {
                                            Toast.makeText(context, "Icon selected!", Toast.LENGTH_SHORT).show()
                                        }
                                ) {
                                    Icon(iconVector, contentDescription = null, tint = Color.White, modifier = Modifier.padding(8.dp))
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newTitle.isNotBlank()) {
                                currentGroupTitle = newTitle
                                showUpdateGroupIconModal = false
                                onSendMessage("✏️ Group details updated: Group name changed to \"$newTitle\"", "TEXT", 0, null, null)
                                Toast.makeText(context, "Group Icon & Title updated!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenPrimary)
                    ) {
                        Text("Update Group", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUpdateGroupIconModal = false }) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
        }

        // Code Snippet Modal
        if (isCodeModalOpen) {
            ModalBottomSheet(
                onDismissRequest = { isCodeModalOpen = false },
                containerColor = WhatsAppDarkHeader
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Paste Code Snippet", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = codeSnippetModalText,
                        onValueChange = { codeSnippetModalText = it },
                        placeholder = { Text("fun main() { println(\"Hello WorkAI\") }") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WhatsAppGreenLight,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = WhatsAppGreenPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isCodeModalOpen = false
                                onSendMessage(
                                    codeSnippetModalText.ifBlank { "fun calculateMetrics() = 100" },
                                    "CODE",
                                    0,
                                    "CodeSnippet.kt",
                                    "1.1 KB"
                                )
                                codeSnippetModalText = ""
                            }
                    ) {
                        Text(
                            text = "Send Code Snippet to AI",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 12.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: MessageEntity,
    userProfile: UserProfileEntity = UserProfileEntity(),
    onCopyText: () -> Unit,
    onStarToggle: () -> Unit,
    onSummarizeFile: () -> Unit
) {
    val isMe = message.isFromMe

    val sentBubbleBg = if (!userProfile.isDarkMode) {
        WhatsAppSentBubbleLight
    } else {
        when (userProfile.bubbleStyle) {
            "MODERN_TEAL" -> Color(0xFF00695C)
            "MINIMAL_GRAPHITE" -> Color(0xFF37474F)
            "VIBRANT_NEON" -> Color(0xFF1A237E)
            "LAVENDER_SOFT" -> Color(0xFF4A148C)
            else -> WhatsAppSentBubbleDark
        }
    }

    val receivedBubbleBg = if (userProfile.isDarkMode) WhatsAppReceivedBubbleDark else WhatsAppReceivedBubbleLight
    val bubbleColor = if (isMe) sentBubbleBg else receivedBubbleBg
    val textBodyColor = if (userProfile.isDarkMode) Color.White else Color(0xFF111B21)
    val timeTextColor = if (userProfile.isDarkMode) Color(0xFF8696A0) else Color(0xFF667781)

    Box(
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (isMe) 12.dp else 2.dp,
                bottomEnd = if (isMe) 2.dp else 12.dp
            ),
            color = bubbleColor,
            shadowElevation = if (!userProfile.isDarkMode) 1.dp else 0.dp,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                // Sender name in group chats
                if (!isMe && message.senderName.isNotBlank()) {
                    Text(
                        text = message.senderName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (userProfile.isDarkMode) WhatsAppGreenLight else WhatsAppGreenTeal,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                // Message Body by Type
                when (message.messageType) {
                    "VOICE" -> {
                        VoiceMessagePlayer(
                            durationSeconds = message.voiceDurationSeconds,
                            transcript = message.voiceTranscript,
                            isFromMe = isMe
                        )
                    }
                    "FILE", "CODE" -> {
                        FileAttachmentCard(
                            fileName = message.fileName ?: "Document.pdf",
                            fileSize = message.fileSize,
                            fileType = if (message.messageType == "CODE") "CODE" else "PDF",
                            contentSnippet = message.content,
                            onSummarizeWithAI = onSummarizeFile
                        )
                    }
                    else -> { // TEXT
                        Text(
                            text = message.content,
                            fontSize = userProfile.fontSizeSp.sp,
                            color = textBodyColor,
                            lineHeight = (userProfile.fontSizeSp + 5).sp
                        )
                    }
                }

                // Timestamp & Status Row
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                ) {
                    if (message.isStarred) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Starred",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(12.dp)
                        )
                    }

                    Text(
                        text = formatMsgTime(message.timestamp),
                        fontSize = 10.sp,
                        color = timeTextColor
                    )

                    if (isMe) {
                        Spacer(modifier = Modifier.width(4.dp))
                        when (message.status) {
                            "SENDING" -> {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Sending",
                                    tint = Color(0xFF8696A0),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            "SENT" -> {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Sent",
                                    tint = Color(0xFF8696A0),
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            "DELIVERED" -> {
                                Icon(
                                    imageVector = Icons.Default.DoneAll,
                                    contentDescription = "Delivered",
                                    tint = Color(0xFF8696A0),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            else -> { // "READ"
                                Icon(
                                    imageVector = Icons.Default.DoneAll,
                                    contentDescription = "Read",
                                    tint = WhatsAppAccentBlue,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    // Actions: Copy & Star
                    IconButton(
                        onClick = onCopyText,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = Color(0xFF8696A0),
                            modifier = Modifier.size(11.dp)
                        )
                    }

                    IconButton(
                        onClick = onStarToggle,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = if (message.isStarred) Icons.Default.Star else Icons.Default.StarOutline,
                            contentDescription = "Star",
                            tint = if (message.isStarred) Color(0xFFFFC107) else Color(0xFF8696A0),
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AttachmentOptionItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = color,
            modifier = Modifier.size(50.dp)
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = Color.White, modifier = Modifier.padding(13.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = title, fontSize = 12.sp, color = Color.White)
    }
}

private fun formatMsgTime(time: Long): String {
    val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(Date(time))
}
