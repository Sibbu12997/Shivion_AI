package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import com.example.data.local.entities.UserProfileEntity
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.AIAssistantEntity
import com.example.data.local.entities.ChatEntity
import com.example.ui.components.QuickAccessWidgetBar
import com.example.ui.theme.WhatsAppDarkBackground
import com.example.ui.theme.WhatsAppDarkHeader
import com.example.ui.theme.WhatsAppGreenLight
import com.example.ui.theme.WhatsAppGreenPrimary
import com.example.ui.theme.WhatsAppUnreadGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatListScreen(
    chats: List<ChatEntity>,
    aiAssistants: List<AIAssistantEntity>,
    userProfile: UserProfileEntity = UserProfileEntity(),
    onChatSelect: (String) -> Unit,
    onCreateGroupClick: () -> Unit,
    onAddAIBotClick: () -> Unit,
    onProfileClick: () -> Unit,
    onStartVideoCallClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onOpenSettingsClick: () -> Unit = {},
    onOpenNavigatorClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val tabs = listOf("All Chats", "AI Assistants", "Multi-AI Groups", "Video Calls")

    val filteredChats = chats.filter { chat ->
        if (searchQuery.isBlank()) true
        else chat.title.contains(searchQuery, ignoreCase = true) || chat.lastMessage.contains(searchQuery, ignoreCase = true)
    }.filter { chat ->
        when (selectedTab) {
            1 -> chat.type == "AI"
            2 -> chat.type == "AI_GROUP"
            else -> true
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                // Top Header Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    if (!isSearchActive) {
                        Text(
                            text = "Sivion AI",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(onClick = { onOpenNavigatorClick() }) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Sivion Navigator", tint = MaterialTheme.colorScheme.onPrimary)
                        }

                        IconButton(onClick = { onStartVideoCallClick() }) {
                            Icon(Icons.Default.VideoCall, contentDescription = "Video Call", tint = MaterialTheme.colorScheme.onPrimary)
                        }

                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onPrimary)
                        }

                        Box {
                            IconButton(onClick = { isMenuExpanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onPrimary)
                            }
                            DropdownMenu(
                                expanded = isMenuExpanded,
                                onDismissRequest = { isMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("⚙️ Settings") },
                                    onClick = { isMenuExpanded = false; onOpenSettingsClick() }
                                )
                                DropdownMenuItem(
                                    text = { Text("🤖 Ask Sivion AI Navigator") },
                                    onClick = { isMenuExpanded = false; onOpenNavigatorClick() }
                                )
                                DropdownMenuItem(
                                    text = { Text("🚀 Create Multi-AI Group") },
                                    onClick = { isMenuExpanded = false; onCreateGroupClick() }
                                )
                                DropdownMenuItem(
                                    text = { Text("🤖 Add Custom AI Assistant") },
                                    onClick = { isMenuExpanded = false; onAddAIBotClick() }
                                )
                                DropdownMenuItem(
                                    text = { Text("📹 Start Group Video Call") },
                                    onClick = { isMenuExpanded = false; onStartVideoCallClick() }
                                )
                                DropdownMenuItem(
                                    text = { Text("👤 User Profile & Cloud Sync") },
                                    onClick = { isMenuExpanded = false; onProfileClick() }
                                )
                                DropdownMenuItem(
                                    text = { Text("🚪 Sign Out") },
                                    onClick = { isMenuExpanded = false; onLogoutClick() }
                                )
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search chats or AI notes...", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            trailingIcon = {
                                IconButton(onClick = { isSearchActive = false; searchQuery = "" }) {
                                    Text("Cancel", color = MaterialTheme.colorScheme.onPrimary, fontSize = 13.sp)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        )
                    }
                }

                // Scrollable Tabs Row
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    edgePadding = 12.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = MaterialTheme.colorScheme.onPrimary,
                            height = 3.dp
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 14.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                                )
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                // Secondary FAB: Create Multi-AI Group
                FloatingActionButton(
                    onClick = { onCreateGroupClick() },
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = WhatsAppGreenPrimary,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .size(44.dp)
                ) {
                    Icon(Icons.Default.GroupAdd, contentDescription = "New Group", modifier = Modifier.size(20.dp))
                }

                // Primary FAB: Add AI Bot / Chat
                FloatingActionButton(
                    onClick = { onAddAIBotClick() },
                    containerColor = WhatsAppGreenPrimary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Chat", modifier = Modifier.size(28.dp))
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Quick Access AI Work Widgets
            QuickAccessWidgetBar(
                onActionClick = { widget ->
                    when (widget.id) {
                        "ask_copilot" -> onChatSelect("chat_work_copilot")
                        "ai_video_call" -> onStartVideoCallClick()
                        else -> onCreateGroupClick()
                    }
                }
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Main List
            if (selectedTab == 3) {
                // Calls View
                CallsTabContent(
                    onStartCall = { onStartVideoCallClick() }
                )
            } else if (filteredChats.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF202C33),
                            modifier = Modifier.size(72.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = null,
                                tint = WhatsAppGreenLight,
                                modifier = Modifier.padding(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchQuery.isNotBlank()) "No results for \"$searchQuery\"" else "No chats found",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Sivion AI Navigator can help you find features, customize settings, or create a new AI team.",
                            fontSize = 13.sp,
                            color = Color(0xFF8696A0),
                            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        // Interactive Next-Step Action Buttons
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = WhatsAppGreenPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenNavigatorClick() }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ask Sivion AI Navigator Bot", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF202C33),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onCreateGroupClick() }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Groups,
                                        contentDescription = null,
                                        tint = WhatsAppGreenLight,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        "New Group",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF202C33),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onOpenSettingsClick() }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null,
                                        tint = WhatsAppGreenLight,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        "Settings",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredChats, key = { it.id }) { chat ->
                        ChatItemRow(
                            chat = chat,
                            onClick = { onChatSelect(chat.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatItemRow(
    chat: ChatEntity,
    onClick: () -> Unit
) {
    val icon = when (chat.avatarIconName) {
        "smart_toy" -> Icons.Default.SmartToy
        "code" -> Icons.Default.Code
        "brush" -> Icons.Default.Brush
        "analytics" -> Icons.Default.Analytics
        "gavel" -> Icons.Default.Gavel
        "groups" -> Icons.Default.Groups
        else -> Icons.Default.Person
    }

    val iconBgColor = when (chat.type) {
        "AI_GROUP" -> Color(0xFF9C27B0)
        "AI" -> WhatsAppGreenPrimary
        else -> Color(0xFF008069)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Avatar with Online indicator
        Box {
            Surface(
                shape = CircleShape,
                color = iconBgColor,
                modifier = Modifier.size(52.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = chat.title,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(28.dp)
                )
            }

            // Online Dot
            Surface(
                shape = CircleShape,
                color = WhatsAppGreenLight,
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.BottomEnd)
            ) {}
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Title and Last Message
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = chat.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.isPinned) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "Pinned",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .size(14.dp)
                    )
                }

                Text(
                    text = formatTimestamp(chat.lastMessageTime),
                    fontSize = 11.sp,
                    color = if (chat.unreadCount > 0) WhatsAppUnreadGreen else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = chat.lastMessage,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.unreadCount > 0) {
                    Surface(
                        shape = CircleShape,
                        color = WhatsAppUnreadGreen,
                        modifier = Modifier.padding(start = 6.dp)
                    ) {
                        Text(
                            text = chat.unreadCount.toString(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CallsTabContent(onStartCall: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = WhatsAppGreenPrimary.copy(alpha = 0.2f),
            modifier = Modifier.size(72.dp)
        ) {
            Icon(
                imageVector = Icons.Default.VideoCall,
                contentDescription = null,
                tint = WhatsAppGreenPrimary,
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Group Video Calls & AI Voice Sync",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Host interactive workspace video meetings with Work AI Copilots and team colleagues with live AI transcriptions.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = WhatsAppGreenPrimary,
            modifier = Modifier.clickable { onStartCall() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Default.VideoCall, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Multi-AI Group Video Call", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

private fun formatTimestamp(time: Long): String {
    val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(Date(time))
}
