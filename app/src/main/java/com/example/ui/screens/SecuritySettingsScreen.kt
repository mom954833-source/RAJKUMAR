package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AutoLockDuration
import com.example.ui.components.FounderBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultBackground
import com.example.ui.theme.VaultSurface

@Composable
fun SecuritySettingsScreen(
    autoLockDuration: AutoLockDuration,
    biometricEnabled: Boolean,
    lockOnScreenOff: Boolean,
    lockOnLeaveForeground: Boolean,
    securityNotifications: Boolean,
    onBack: () -> Unit,
    onUpdateAutoLock: (AutoLockDuration) -> Unit,
    onUpdateBiometric: (Boolean) -> Unit,
    onUpdateLockScreenOff: (Boolean) -> Unit,
    onUpdateLockLeaveForeground: (Boolean) -> Unit,
    onUpdateSecurityNotifications: (Boolean) -> Unit,
    onChangePin: (current: String, newPin: String) -> Boolean,
    onChangePassword: (current: String, newPwd: String) -> Boolean,
    onNavigateLogs: () -> Unit,
    onResetVault: () -> Unit
) {
    val scrollState = rememberScrollState()

    var showChangePinDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showAutoLockDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("security_settings_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("security_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = "Security & Encryption",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("security_title")
                )
                Text(
                    text = "Manage biometric, PIN, and auto-lock rules",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Authentication Methods Section
        SectionTitle(title = "AUTHENTICATION")

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                SettingsClickableRow(
                    icon = Icons.Default.Lock,
                    iconColor = ElectricBlue,
                    title = "Change PIN",
                    subtitle = "Update master 4-digit numeric PIN",
                    onClick = { showChangePinDialog = true },
                    testTag = "row_change_pin"
                )

                SettingsClickableRow(
                    icon = Icons.Default.Password,
                    iconColor = CyberPurple,
                    title = "Change Password",
                    subtitle = "Update master vault recovery password",
                    onClick = { showChangePasswordDialog = true },
                    testTag = "row_change_password"
                )

                SettingsToggleRow(
                    icon = Icons.Default.Fingerprint,
                    iconColor = GoldenYellow,
                    title = "Enable Biometrics",
                    subtitle = "Unlock via Fingerprint or Face recognition",
                    checked = biometricEnabled,
                    onCheckedChange = onUpdateBiometric,
                    testTag = "toggle_biometrics"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Auto Lock Rules Section
        SectionTitle(title = "AUTO LOCK RULES")

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                SettingsClickableRow(
                    icon = Icons.Default.Timer,
                    iconColor = ElectricBlue,
                    title = "Auto Lock Timeout",
                    subtitle = autoLockDuration.label,
                    onClick = { showAutoLockDialog = true },
                    testTag = "row_auto_lock"
                )

                SettingsToggleRow(
                    icon = Icons.Default.PhoneAndroid,
                    iconColor = CyberPurple,
                    title = "Lock on Screen Off",
                    subtitle = "Instantly secure vault when device screen sleeps",
                    checked = lockOnScreenOff,
                    onCheckedChange = onUpdateLockScreenOff,
                    testTag = "toggle_lock_screen_off"
                )

                SettingsToggleRow(
                    icon = Icons.Default.Lock,
                    iconColor = GoldenYellow,
                    title = "Lock on Leaving Foreground",
                    subtitle = "Require authentication when returning to app",
                    checked = lockOnLeaveForeground,
                    onCheckedChange = onUpdateLockLeaveForeground,
                    testTag = "toggle_lock_leave_foreground"
                )

                SettingsToggleRow(
                    icon = Icons.Default.Notifications,
                    iconColor = Color(0xFF38BDF8),
                    title = "Security Notifications",
                    subtitle = "Notify on unauthorized or repeated unlock attempts",
                    checked = securityNotifications,
                    onCheckedChange = onUpdateSecurityNotifications,
                    testTag = "toggle_notifications"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Audit Logs Section
        SectionTitle(title = "AUDIT & LOGS")

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                SettingsClickableRow(
                    icon = Icons.Default.Shield,
                    iconColor = ElectricBlue,
                    title = "Security Logs",
                    subtitle = "Chronological unlock events and failed attempts",
                    onClick = onNavigateLogs,
                    testTag = "row_security_logs"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Danger Zone Section
        SectionTitle(title = "DANGER ZONE")

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = CrimsonDanger.copy(alpha = 0.4f),
            backgroundColor = Color(0xFF1F1116)
        ) {
            SettingsClickableRow(
                icon = Icons.Default.Restore,
                iconColor = CrimsonDanger,
                title = "Reset Vault",
                subtitle = "Permanently clear all protected apps, keys, and logs",
                onClick = { showResetDialog = true },
                testTag = "row_reset_vault"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        FounderBadge(detailed = true)
        Spacer(modifier = Modifier.height(20.dp))
    }

    // Change PIN Dialog
    if (showChangePinDialog) {
        ChangePinDialog(
            onDismiss = { showChangePinDialog = false },
            onSubmit = onChangePin
        )
    }

    // Change Password Dialog
    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showChangePasswordDialog = false },
            onSubmit = onChangePassword
        )
    }

    // Auto Lock Options Dialog
    if (showAutoLockDialog) {
        AutoLockSelectionDialog(
            current = autoLockDuration,
            onDismiss = { showAutoLockDialog = false },
            onSelect = {
                onUpdateAutoLock(it)
                showAutoLockDialog = false
            }
        )
    }

    // Reset Confirmation Dialog
    if (showResetDialog) {
        ResetVaultDialog(
            onDismiss = { showResetDialog = false },
            onConfirm = {
                onResetVault()
                showResetDialog = false
            }
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsClickableRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = TextSecondary, fontSize = 12.sp)
            }
        }
        Icon(Icons.Default.ChevronRight, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = TextSecondary, fontSize = 12.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = iconColor
            )
        )
    }
}

@Composable
private fun ChangePinDialog(
    onDismiss: () -> Unit,
    onSubmit: (current: String, newPin: String) -> Boolean
) {
    var currentPin by remember { mutableStateOf("") }
    var newPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VaultSurface,
        title = { Text("Change Master PIN", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = currentPin,
                    onValueChange = { if (it.length <= 4) currentPin = it },
                    label = { Text("Current 4-Digit PIN") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPin,
                    onValueChange = { if (it.length <= 4) newPin = it },
                    label = { Text("New 4-Digit PIN") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPin,
                    onValueChange = { if (it.length <= 4) confirmPin = it },
                    label = { Text("Confirm New PIN") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorText != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(errorText!!, color = CrimsonDanger, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPin.length != 4) {
                        errorText = "New PIN must be 4 digits"
                        return@Button
                    }
                    if (newPin != confirmPin) {
                        errorText = "New PINs do not match"
                        return@Button
                    }
                    val success = onSubmit(currentPin, newPin)
                    if (success) {
                        onDismiss()
                    } else {
                        errorText = "Current PIN is incorrect"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
            ) {
                Text("Update PIN")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
private fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onSubmit: (current: String, newPwd: String) -> Boolean
) {
    var currentPwd by remember { mutableStateOf("") }
    var newPwd by remember { mutableStateOf("") }
    var confirmPwd by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VaultSurface,
        title = { Text("Change Master Password", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = currentPwd,
                    onValueChange = { currentPwd = it },
                    label = { Text("Current Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPwd,
                    onValueChange = { newPwd = it },
                    label = { Text("New Password (min 6 chars)") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPwd,
                    onValueChange = { confirmPwd = it },
                    label = { Text("Confirm New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorText != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(errorText!!, color = CrimsonDanger, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPwd.length < 6) {
                        errorText = "Password must be at least 6 characters"
                        return@Button
                    }
                    if (newPwd != confirmPwd) {
                        errorText = "Passwords do not match"
                        return@Button
                    }
                    val success = onSubmit(currentPwd, newPwd)
                    if (success) {
                        onDismiss()
                    } else {
                        errorText = "Current password is incorrect"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberPurple)
            ) {
                Text("Update Password")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
private fun AutoLockSelectionDialog(
    current: AutoLockDuration,
    onDismiss: () -> Unit,
    onSelect: (AutoLockDuration) -> Unit
) {
    val options = listOf(
        AutoLockDuration.IMMEDIATE,
        AutoLockDuration.SECONDS_30,
        AutoLockDuration.MINUTE_1,
        AutoLockDuration.MINUTES_5
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VaultSurface,
        title = { Text("Auto Lock Timeout", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                options.forEach { option ->
                    val isSelected = (option == current)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(option) }
                            .padding(vertical = 12.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = option.label,
                            color = if (isSelected) ElectricBlue else TextPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 15.sp
                        )
                        if (isSelected) {
                            Icon(Icons.Default.Lock, null, tint = ElectricBlue, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
private fun ResetVaultDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VaultSurface,
        title = { Text("Reset Vault Permanently?", color = CrimsonDanger, fontWeight = FontWeight.Bold) },
        text = {
            Text(
                "This action cannot be undone. All protected apps, master PIN, master password, and audit records will be permanently erased.",
                color = TextSecondary,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonDanger)
            ) {
                Text("Erase & Reset Vault")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}
