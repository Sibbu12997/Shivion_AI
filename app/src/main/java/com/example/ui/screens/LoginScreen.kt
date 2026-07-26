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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppDarkBackground
import com.example.ui.theme.WhatsAppGreenLight
import com.example.ui.theme.WhatsAppGreenPrimary

@Composable
fun LoginScreen(
    onLoginSuccess: (name: String, role: String, phone: String, department: String, level: String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Mobile Number, 1 = Work Email & Role, 2 = Security PIN
    var phoneNumber by remember { mutableStateOf("+1 555-019-2834") }
    var otpCode by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("Alex Rivera") }
    var role by remember { mutableStateOf("VP of Product & AI Strategy") }
    var department by remember { mutableStateOf("Product & AI Ops") }
    var managementLevel by remember { mutableStateOf("Super Admin") }
    var pinCode by remember { mutableStateOf("1234") }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        WhatsAppDarkBackground,
                        Color(0xFF071B16),
                        WhatsAppDarkBackground
                    )
                )
            )
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // App Branding Icon
            Surface(
                shape = CircleShape,
                color = WhatsAppGreenPrimary,
                modifier = Modifier.size(76.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "WorkAI Logo",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "WorkAI Enterprise",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Secure Workspace Chat • Multi-AI & Team Hierarchy",
                fontSize = 12.sp,
                color = Color(0xFF8696A0),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            // Auth Method Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF1F2C34),
                contentColor = WhatsAppGreenPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Mobile Phone", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Work Role", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("PIN Passcode", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }

            // Form Input Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF111B21)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    when (selectedTab) {
                        0 -> {
                            // Mobile Phone Login Flow
                            Text(
                                text = "Login via Mobile Number",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = { Text("Mobile Phone Number (+ Country Code)") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = WhatsAppGreenPrimary) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WhatsAppGreenPrimary,
                                    unfocusedBorderColor = Color(0xFF2A3942),
                                    focusedLabelColor = WhatsAppGreenPrimary,
                                    unfocusedLabelColor = Color(0xFF8696A0),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            if (isOtpSent) {
                                OutlinedTextField(
                                    value = otpCode,
                                    onValueChange = { if (it.length <= 6) otpCode = it },
                                    label = { Text("6-Digit OTP Verification Code") },
                                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = WhatsAppGreenPrimary) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = WhatsAppGreenPrimary,
                                        unfocusedBorderColor = Color(0xFF2A3942),
                                        focusedLabelColor = WhatsAppGreenPrimary,
                                        unfocusedLabelColor = Color(0xFF8696A0),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                                )
                            }

                            Button(
                                onClick = {
                                    if (!isOtpSent) {
                                        isOtpSent = true
                                        Toast.makeText(context, "SMS Verification OTP code sent to $phoneNumber", Toast.LENGTH_SHORT).show()
                                    } else {
                                        onLoginSuccess(name, role, phoneNumber, department, managementLevel)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (!isOtpSent) "Send SMS OTP Code" else "Verify OTP & Enter Workspace",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        1 -> {
                            // Work Role & Department Hierarchy Login Flow
                            Text(
                                text = "Work AI Credentials & Hierarchy",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Full Name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = WhatsAppGreenPrimary) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WhatsAppGreenPrimary,
                                    unfocusedBorderColor = Color(0xFF2A3942),
                                    focusedLabelColor = WhatsAppGreenPrimary,
                                    unfocusedLabelColor = Color(0xFF8696A0),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                            )

                            OutlinedTextField(
                                value = role,
                                onValueChange = { role = it },
                                label = { Text("Work Designation / Role Title") },
                                leadingIcon = { Icon(Icons.Default.Work, contentDescription = null, tint = WhatsAppGreenPrimary) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WhatsAppGreenPrimary,
                                    unfocusedBorderColor = Color(0xFF2A3942),
                                    focusedLabelColor = WhatsAppGreenPrimary,
                                    unfocusedLabelColor = Color(0xFF8696A0),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                            ) {
                                OutlinedTextField(
                                    value = department,
                                    onValueChange = { department = it },
                                    label = { Text("Department") },
                                    leadingIcon = { Icon(Icons.Default.Business, contentDescription = null, tint = WhatsAppGreenPrimary) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = WhatsAppGreenPrimary,
                                        unfocusedBorderColor = Color(0xFF2A3942),
                                        focusedLabelColor = WhatsAppGreenPrimary,
                                        unfocusedLabelColor = Color(0xFF8696A0),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                OutlinedTextField(
                                    value = managementLevel,
                                    onValueChange = { managementLevel = it },
                                    label = { Text("Level") },
                                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = WhatsAppGreenPrimary) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = WhatsAppGreenPrimary,
                                        unfocusedBorderColor = Color(0xFF2A3942),
                                        focusedLabelColor = WhatsAppGreenPrimary,
                                        unfocusedLabelColor = Color(0xFF8696A0),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Button(
                                onClick = {
                                    onLoginSuccess(
                                        name.ifBlank { "Alex Rivera" },
                                        role.ifBlank { "Specialist" },
                                        phoneNumber,
                                        department,
                                        managementLevel
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Launch Enterprise Workspace", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        2 -> {
                            // PIN Mode
                            Text(
                                text = "Enter Enterprise PIN Code",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            OutlinedTextField(
                                value = pinCode,
                                onValueChange = { if (it.length <= 4) pinCode = it },
                                label = { Text("4-Digit Passcode") },
                                leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = WhatsAppGreenPrimary) },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WhatsAppGreenPrimary,
                                    unfocusedBorderColor = Color(0xFF2A3942),
                                    focusedLabelColor = WhatsAppGreenPrimary,
                                    unfocusedLabelColor = Color(0xFF8696A0),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )

                            Button(
                                onClick = {
                                    onLoginSuccess(name, role, phoneNumber, department, managementLevel)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreenPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text("Authenticate PIN & Open", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF182229)
            ) {
                Text(
                    text = "🔒 End-to-End Encrypted • Super Admin & Corporate Role Control Active",
                    fontSize = 11.sp,
                    color = Color(0xFF8696A0),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
    }
}
