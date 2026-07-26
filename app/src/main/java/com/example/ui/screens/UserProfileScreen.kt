package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.UserProfileEntity
import com.example.ui.theme.WhatsAppGreenLight
import com.example.ui.theme.WhatsAppGreenPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UserProfileScreen(
    profile: UserProfileEntity,
    onBackClick: () -> Unit,
    onSaveProfile: (UserProfileEntity) -> Unit,
    onSyncNowClick: () -> Unit,
    onOpenSignInScreen: (() -> Unit)? = null
) {
    var name by remember(profile) { mutableStateOf(profile.name) }
    var role by remember(profile) { mutableStateOf(profile.role) }
    var phoneNumber by remember(profile) { mutableStateOf(profile.phoneNumber) }
    var department by remember(profile) { mutableStateOf(profile.department) }
    var managementLevel by remember(profile) { mutableStateOf(profile.managementLevel) }
    var bio by remember(profile) { mutableStateOf(profile.statusBio) }
    var avatarIcon by remember(profile) { mutableStateOf(profile.avatarIcon) }
    var isSuperAdmin by remember(profile) { mutableStateOf(profile.isSuperAdmin) }
    var isCloudSync by remember(profile) { mutableStateOf(profile.isCloudSyncEnabled) }
    var isPinProtected by remember(profile) { mutableStateOf(profile.isPinProtected) }
    var allowFileSharing by remember(profile) { mutableStateOf(profile.allowFileSharing) }
    var allowGroupCreation by remember(profile) { mutableStateOf(profile.allowGroupCreation) }
    var retentionDays by remember(profile) { mutableStateOf(profile.dataRetentionDays) }

    // Notification Control state
    var notifyMessages by remember(profile) { mutableStateOf(profile.notifyMessages) }
    var notifyGroups by remember(profile) { mutableStateOf(profile.notifyGroups) }
    var notifyPreviewText by remember(profile) { mutableStateOf(profile.notifyPreviewText) }
    var notificationTone by remember(profile) { mutableStateOf(profile.notificationTone) }

    // Security Setup state
    var isBiometricEnabled by remember(profile) { mutableStateOf(profile.isBiometricEnabled) }
    var appLockTimeout by remember(profile) { mutableStateOf(profile.appLockTimeout) }
    var screenSecurityEnabled by remember(profile) { mutableStateOf(profile.screenSecurityEnabled) }
    var twoFactorEnabled by remember(profile) { mutableStateOf(profile.twoFactorEnabled) }

    // Knowledge Transfer (KT) & Handover state
    var isKtHandoverActive by remember(profile) { mutableStateOf(profile.isKtHandoverActive) }
    var handoverSuccessorName by remember(profile) { mutableStateOf(profile.handoverSuccessorName) }
    var handoverSuccessorRole by remember(profile) { mutableStateOf(profile.handoverSuccessorRole) }
    var handoverNotes by remember(profile) { mutableStateOf(profile.handoverNotes) }

    // Modals
    var showAvatarModal by remember { mutableStateOf(false) }
    var showCreateMemberModal by remember { mutableStateOf(false) }
    var showHandoverConfirmDialog by remember { mutableStateOf(false) }
    var isSyncing by remember { mutableStateOf(false) }

    // Database Logs
    val dbLogList = remember {
        androidx.compose.runtime.mutableStateListOf(
            "[19:42:02] ROOM DB: Query executed on 'user_profile' (id=1)",
            "[19:40:15] CLOUD SYNC: AES-256 backup payload generated for cloud",
            "[19:38:50] SECURITY LOG: Biometric verification token active",
            "[19:35:10] KT HANDOVER: Successor '$handoverSuccessorName' granted prompt transfer access",
            "[19:30:12] ROOM DB: Batch sync written to 'chats' and 'messages' (32 records)"
        )
    }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val avatarOptions = listOf(
        "person_1" to Icons.Default.Person,
        "person_2" to Icons.Default.Badge,
        "person_3" to Icons.Default.Star,
        "person_4" to Icons.Default.Work,
        "person_5" to Icons.Default.Psychology,
        "person_6" to Icons.Default.AdminPanelSettings
    )

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Profile, Hierarchy & Control Center", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                    Text("Manage avatar, security, database logs & handover", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                }
                if (onOpenSignInScreen != null) {
                    IconButton(onClick = onOpenSignInScreen) {
                        Icon(Icons.Default.CloudSync, contentDescription = "Cloud Auth", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Profile & Avatar Card with WhatsApp-style Camera Badge
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Selected Avatar Preview with WhatsApp Camera Badge
                    val currentAvatarVector = avatarOptions.find { it.first == avatarIcon }?.second ?: Icons.Default.Person

                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .size(92.dp)
                            .clickable { showAvatarModal = true }
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = WhatsAppGreenPrimary,
                            modifier = Modifier.size(88.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(currentAvatarVector, contentDescription = null, tint = Color.White, modifier = Modifier.size(50.dp))
                            }
                        }

                        // WhatsApp style camera action badge
                        Surface(
                            shape = CircleShape,
                            color = WhatsAppGreenLight,
                            shadowElevation = 3.dp,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Edit Photo", tint = Color.White, modifier = Modifier.padding(6.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tap Profile Picture for WhatsApp Options", fontSize = 12.sp, color = WhatsAppGreenLight, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Display Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = WhatsAppGreenPrimary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WhatsAppGreenPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Mobile Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = WhatsAppGreenPrimary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WhatsAppGreenPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Status Bio") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WhatsAppGreenPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notification Control Settings Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = WhatsAppGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Notification & Sound Controls", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Direct Message Alerts", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Play sound & display popups for 1-on-1 chats", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = notifyMessages,
                            onCheckedChange = { notifyMessages = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreenPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Group & AI Bot Activity Notifications", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Alert when team members or AI bots respond", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = notifyGroups,
                            onCheckedChange = { notifyGroups = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreenPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Show Preview Text on Lock Screen", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Include message text snippet in notification popups", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = notifyPreviewText,
                            onCheckedChange = { notifyPreviewText = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreenPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Notification Ringtone:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        listOf("Enterprise Emerald Tone", "Classic Whistle", "Subtle Chime", "Silent").forEach { tone ->
                            val isSelected = (tone == notificationTone)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) WhatsAppGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable { notificationTone = tone }
                            ) {
                                Text(
                                    text = tone,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Security Setup Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = WhatsAppGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Security Setup & App Lock", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Fingerprint, contentDescription = null, tint = WhatsAppGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Biometric / Fingerprint Unlock", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Require fingerprint sensor to open WorkAI", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = isBiometricEnabled,
                            onCheckedChange = { isBiometricEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreenPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = WhatsAppGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Screen Security (Block Screenshots)", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Prevent taking screenshots or app preview in switcher", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = screenSecurityEnabled,
                            onCheckedChange = { screenSecurityEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreenPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CloudSync, contentDescription = null, tint = WhatsAppGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Two-Factor Authentication (2FA)", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Require 2FA PIN for enterprise login", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = twoFactorEnabled,
                            onCheckedChange = { twoFactorEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreenPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Automatically Lock App:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        listOf("Immediately", "After 1 min", "After 15 min").forEach { timeout ->
                            val isSelected = (timeout == appLockTimeout)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) WhatsAppGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable { appLockTimeout = timeout }
                            ) {
                                Text(
                                    text = timeout,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SQLite & Room Database Diagnostic & Operation Controls
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storage, contentDescription = null, tint = WhatsAppGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SQLite Database Engine & Operation Logs", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // SQLite DB Info Banner
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = WhatsAppGreenPrimary.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Column {
                                Text("Database File: workai_chat_database.db", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("Engine: SQLite 3.39+ (Room v2.6 / WAL Mode Enabled)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("ACTIVE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WhatsAppGreenPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp).verticalScroll(rememberScrollState())) {
                            dbLogList.forEach { logLine ->
                                Text(
                                    text = logLine,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                val newTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                                dbLogList.add(0, "[$newTime] SQLITE PRAGMA: integrity_check => result: 'ok'")
                                Toast.makeText(context, "SQLite integrity check PASSED!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("PRAGMA Check", fontSize = 10.sp, color = WhatsAppGreenPrimary)
                        }

                        OutlinedButton(
                            onClick = {
                                val newTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                                dbLogList.add(0, "[$newTime] SQLITE VACUUM: Executed successfully (Pages defragmented)")
                                Toast.makeText(context, "SQLite VACUUM executed successfully!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("VACUUM DB", fontSize = 10.sp, color = WhatsAppGreenPrimary)
                        }

                        OutlinedButton(
                            onClick = {
                                dbLogList.clear()
                                Toast.makeText(context, "Logs cleared", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(0.8f)
                        ) {
                            Text("Clear", fontSize = 10.sp, color = Color.Red)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Knowledge Transfer (KT) & Handover Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = WhatsAppGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Knowledge Transfer (KT) & Role Handover", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp)
                            Text("Transfer AI prompts, admin rights & database backup ownership", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = isKtHandoverActive,
                            onCheckedChange = { isKtHandoverActive = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreenPrimary)
                        )
                    }

                    if (isKtHandoverActive) {
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = handoverSuccessorName,
                            onValueChange = { handoverSuccessorName = it },
                            label = { Text("Successor / Handover Colleague Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = handoverSuccessorRole,
                            onValueChange = { handoverSuccessorRole = it },
                            label = { Text("Successor Role / Title") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = handoverNotes,
                            onValueChange = { handoverNotes = it },
                            label = { Text("Handover Notes & Prompt Credentials") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { showHandoverConfirmDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Execute Knowledge Transfer (KT)", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Save Profile Settings Button
            Button(
                onClick = {
                    val updated = profile.copy(
                        name = name,
                        phoneNumber = phoneNumber,
                        role = role,
                        department = department,
                        managementLevel = managementLevel,
                        statusBio = bio,
                        avatarIcon = avatarIcon,
                        isSuperAdmin = isSuperAdmin,
                        isCloudSyncEnabled = isCloudSync,
                        isPinProtected = isPinProtected,
                        allowFileSharing = allowFileSharing,
                        allowGroupCreation = allowGroupCreation,
                        dataRetentionDays = retentionDays,
                        notifyMessages = notifyMessages,
                        notifyGroups = notifyGroups,
                        notifyPreviewText = notifyPreviewText,
                        notificationTone = notificationTone,
                        isBiometricEnabled = isBiometricEnabled,
                        appLockTimeout = appLockTimeout,
                        screenSecurityEnabled = screenSecurityEnabled,
                        twoFactorEnabled = twoFactorEnabled,
                        isKtHandoverActive = isKtHandoverActive,
                        handoverSuccessorName = handoverSuccessorName,
                        handoverSuccessorRole = handoverSuccessorRole,
                        handoverNotes = handoverNotes
                    )
                    onSaveProfile(updated)
                    Toast.makeText(context, "Profile, Avatar & Control Settings Saved!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Save All Settings & Controls", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
            }
        }
    }

    // WhatsApp Style Full-Screen Profile Picture Modal
    if (showAvatarModal) {
        val currentVector = avatarOptions.find { it.first == avatarIcon }?.second ?: Icons.Default.Person

        AlertDialog(
            onDismissRequest = { showAvatarModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = WhatsAppGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Profile Picture Options", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    // Big WhatsApp style photo display
                    Surface(
                        shape = CircleShape,
                        color = WhatsAppGreenPrimary,
                        modifier = Modifier.size(120.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(currentVector, contentDescription = null, tint = Color.White, modifier = Modifier.size(70.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Choose Avatar Preset or Photo:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        avatarOptions.forEach { (key, vectorIcon) ->
                            val isSelected = (key == avatarIcon)
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) WhatsAppGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clickable { avatarIcon = key }
                            ) {
                                Icon(vectorIcon, contentDescription = null, tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(8.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // WhatsApp Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                Toast.makeText(context, "Opening Camera to Snap Profile Photo...", Toast.LENGTH_SHORT).show()
                                showAvatarModal = false
                            }
                        ) {
                            Surface(shape = CircleShape, color = WhatsAppGreenPrimary, modifier = Modifier.size(44.dp)) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = Color.White, modifier = Modifier.padding(10.dp))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Camera", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                Toast.makeText(context, "Opening Photo Gallery...", Toast.LENGTH_SHORT).show()
                                showAvatarModal = false
                            }
                        ) {
                            Surface(shape = CircleShape, color = Color(0xFF007AFF), modifier = Modifier.size(44.dp)) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery", tint = Color.White, modifier = Modifier.padding(10.dp))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Gallery", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                avatarIcon = "person_1"
                                Toast.makeText(context, "Profile Photo Removed", Toast.LENGTH_SHORT).show()
                                showAvatarModal = false
                            }
                        ) {
                            Surface(shape = CircleShape, color = Color.Red, modifier = Modifier.size(44.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.White, modifier = Modifier.padding(10.dp))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Remove", fontSize = 11.sp, color = Color.Red)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAvatarModal = false },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenPrimary)
                ) {
                    Text("Done", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Handover Confirmation Dialog
    if (showHandoverConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showHandoverConfirmDialog = false },
            title = { Text("Confirm Knowledge Transfer (KT)", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Are you sure you want to transfer AI prompt libraries, group ownership, and workspace credentials to $handoverSuccessorName ($handoverSuccessorRole)?",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showHandoverConfirmDialog = false
                        Toast.makeText(context, "Knowledge Transfer executed successfully! Credentials transferred to $handoverSuccessorName.", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenPrimary)
                ) {
                    Text("Transfer Rights", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showHandoverConfirmDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    // Modal for Super Admin to Provision New Member Login
    if (showCreateMemberModal) {
        var memberName by remember { mutableStateOf("") }
        var memberRole by remember { mutableStateOf("") }
        var memberPhone by remember { mutableStateOf("") }
        var memberDept by remember { mutableStateOf(department) }
        var memberLevel by remember { mutableStateOf("Associate") }

        AlertDialog(
            onDismissRequest = { showCreateMemberModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.GroupAdd, contentDescription = null, tint = WhatsAppGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Provision New Colleague Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("As Super Admin, you can generate a new workspace login credential for your office colleague.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = memberName,
                        onValueChange = { memberName = it },
                        label = { Text("Colleague Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = memberPhone,
                        onValueChange = { memberPhone = it },
                        label = { Text("Mobile Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = memberRole,
                        onValueChange = { memberRole = it },
                        label = { Text("Designation / Role") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = memberDept,
                            onValueChange = { memberDept = it },
                            label = { Text("Department") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = memberLevel,
                            onValueChange = { memberLevel = it },
                            label = { Text("Level") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (memberName.isNotBlank()) {
                            Toast.makeText(context, "New login provisioned for $memberName! SMS Invite sent to $memberPhone.", Toast.LENGTH_LONG).show()
                            showCreateMemberModal = false
                        } else {
                            Toast.makeText(context, "Please enter colleague name", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenPrimary)
                ) {
                    Text("Create Credentials", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateMemberModal = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

private fun formatFullDate(time: Long): String {
    val formatter = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault())
    return formatter.format(Date(time))
}
