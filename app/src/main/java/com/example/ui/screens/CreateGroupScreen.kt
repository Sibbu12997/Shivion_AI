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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.AIAssistantEntity
import com.example.ui.theme.WhatsAppGreenLight
import com.example.ui.theme.WhatsAppGreenPrimary

data class GroupMemberItem(
    val id: String,
    val name: String,
    val role: String,
    val isAI: Boolean,
    val iconName: String
)

@Composable
fun CreateGroupScreen(
    assistants: List<AIAssistantEntity>,
    onBackClick: () -> Unit,
    onCreateGroupSubmit: (title: String, selectedAssistantIds: List<String>) -> Unit
) {
    var groupTitle by remember { mutableStateOf("🚀 Product & Engineering Team") }
    var selectedGroupIcon by remember { mutableStateOf("groups") }
    val context = LocalContext.current

    val iconOptions = listOf(
        "groups" to Icons.Default.Groups,
        "rocket" to Icons.Default.RocketLaunch,
        "code" to Icons.Default.Code,
        "analytics" to Icons.Default.Analytics,
        "brain" to Icons.Default.Psychology,
        "star" to Icons.Default.Star
    )

    // Pre-populated office colleagues + AI assistants
    val officeColleagues = listOf(
        GroupMemberItem("colleague_sarah", "Sarah Chen", "Director of Product Management", false, "person"),
        GroupMemberItem("colleague_david", "David Miller", "Principal Software Architect", false, "person"),
        GroupMemberItem("colleague_priya", "Priya Sharma", "Lead AI Research Scientist", false, "person"),
        GroupMemberItem("colleague_marcus", "Marcus Vance", "VP of Operations", false, "person"),
        GroupMemberItem("colleague_elena", "Elena Rostova", "Senior UX Designer", false, "person")
    )

    val aiMembers = assistants.map { bot ->
        GroupMemberItem(bot.id, bot.name, bot.roleTitle, true, bot.iconName)
    }

    val allMembers = officeColleagues + aiMembers
    val selectedMemberIds = remember { mutableStateListOf("colleague_sarah", "colleague_david", "ai_copilot", "ai_code_genius") }

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
                Column {
                    Text("Create Team Group & Add Members", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                    Text("Combine Office Colleagues & AI Assistants in one chat", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Group Icon Selector + Group Name Input
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                val currentIconVector = iconOptions.find { it.first == selectedGroupIcon }?.second ?: Icons.Default.Groups
                Surface(
                    shape = CircleShape,
                    color = WhatsAppGreenPrimary,
                    modifier = Modifier
                        .size(52.dp)
                        .clickable {
                            val nextIndex = (iconOptions.indexOfFirst { it.first == selectedGroupIcon } + 1) % iconOptions.size
                            selectedGroupIcon = iconOptions[nextIndex].first
                            Toast.makeText(context, "Group Icon updated!", Toast.LENGTH_SHORT).show()
                        }
                ) {
                    Icon(imageVector = currentIconVector, contentDescription = null, tint = Color.White, modifier = Modifier.padding(12.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedTextField(
                    value = groupTitle,
                    onValueChange = { groupTitle = it },
                    label = { Text("Group Title") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = WhatsAppGreenLight,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                        focusedLabelColor = WhatsAppGreenLight,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            // Group Icon Quick Options
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Icon:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.CenterVertically))
                iconOptions.forEach { (key, vectorIcon) ->
                    val isSelected = (key == selectedGroupIcon)
                    Surface(
                        shape = CircleShape,
                        color = if (isSelected) WhatsAppGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .size(34.dp)
                            .clickable { selectedGroupIcon = key }
                    ) {
                        Icon(vectorIcon, contentDescription = null, tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(8.dp))
                    }
                }
            }

            Text(
                text = "Select Colleagues & AI Assistants (${selectedMemberIds.size} Selected):",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Combined Members List
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(allMembers, key = { it.id }) { member ->
                    val isSelected = selectedMemberIds.contains(member.id)

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                if (isSelected) selectedMemberIds.remove(member.id) else selectedMemberIds.add(member.id)
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (member.isAI) WhatsAppGreenPrimary else Color(0xFF007AFF),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = if (member.isAI) Icons.Default.SmartToy else Icons.Default.Person,
                                    contentDescription = member.name,
                                    tint = Color.White,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(member.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (member.isAI) WhatsAppGreenPrimary.copy(alpha = 0.2f) else Color(0xFF007AFF).copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = if (member.isAI) "AI BOT" else "COLLEAGUE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (member.isAI) WhatsAppGreenPrimary else Color(0xFF007AFF),
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(member.role, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                            }

                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = {
                                    if (it) selectedMemberIds.add(member.id) else selectedMemberIds.remove(member.id)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = WhatsAppGreenPrimary,
                                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (groupTitle.isNotBlank() && selectedMemberIds.isNotEmpty()) {
                        onCreateGroupSubmit(groupTitle, selectedMemberIds.toList())
                    } else {
                        Toast.makeText(context, "Please enter a group title and select members", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create Group with ${selectedMemberIds.size} Members", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
