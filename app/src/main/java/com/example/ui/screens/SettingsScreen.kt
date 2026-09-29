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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FounderBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultBackground

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateSecurity: () -> Unit,
    onNavigateSecurityLogs: () -> Unit,
    onNavigateAbout: () -> Unit,
    onNavigatePrivacy: () -> Unit,
    onNavigateTerms: () -> Unit,
    onNavigateWebsite: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("settings_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("settings_back_btn")
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
                    text = "Settings",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("settings_title")
                )
                Text(
                    text = "Vault preferences and application info",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Account & Vault Card
        Text(
            text = "ACCOUNT & STATUS",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(ElectricBlue.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = ElectricBlue, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Device Owner Vault",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Local offline cryptographic vault",
                        color = EmeraldSuccess,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Security Shortcuts
        Text(
            text = "SECURITY CONTROLS",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                SettingsItem(
                    icon = Icons.Default.Security,
                    color = ElectricBlue,
                    title = "Security & Encryption",
                    subtitle = "Master vault rules and hardware locks",
                    onClick = onNavigateSecurity,
                    testTag = "setting_security"
                )
                SettingsItem(
                    icon = Icons.Default.Fingerprint,
                    color = CyberPurple,
                    title = "Biometric Authentication",
                    subtitle = "Fingerprint / Face recognition settings",
                    onClick = onNavigateSecurity,
                    testTag = "setting_biometric"
                )
                SettingsItem(
                    icon = Icons.Default.Lock,
                    color = GoldenYellow,
                    title = "PIN & Password",
                    subtitle = "Update master credentials",
                    onClick = onNavigateSecurity,
                    testTag = "setting_pin"
                )
                SettingsItem(
                    icon = Icons.Default.Timer,
                    color = Color(0xFF38BDF8),
                    title = "Auto Lock",
                    subtitle = "Inactivity timeout and screen sleep locking",
                    onClick = onNavigateSecurity,
                    testTag = "setting_auto_lock"
                )
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    color = EmeraldSuccess,
                    title = "Notifications",
                    subtitle = "Intruder attempt & lock status alerts",
                    onClick = onNavigateSecurity,
                    testTag = "setting_notifications"
                )
                SettingsItem(
                    icon = Icons.Default.Shield,
                    color = ElectricBlue,
                    title = "Security Logs",
                    subtitle = "Audit history of unlock and security events",
                    onClick = onNavigateSecurityLogs,
                    testTag = "setting_security_logs"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Legal & Info
        Text(
            text = "ABOUT & LEGAL",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                SettingsItem(
                    icon = Icons.Default.Language,
                    color = ElectricBlue,
                    title = "Official Public Website",
                    subtitle = "Explore features, security docs & overview",
                    onClick = onNavigateWebsite,
                    testTag = "setting_website"
                )
                SettingsItem(
                    icon = Icons.Default.Policy,
                    color = EmeraldSuccess,
                    title = "Privacy Policy",
                    subtitle = "Zero data collection disclosure",
                    onClick = onNavigatePrivacy,
                    testTag = "setting_privacy"
                )
                SettingsItem(
                    icon = Icons.Default.Description,
                    color = Color(0xFF94A3B8),
                    title = "Terms of Service",
                    subtitle = "Legitimate device owner usage terms",
                    onClick = onNavigateTerms,
                    testTag = "setting_terms"
                )
                SettingsItem(
                    icon = Icons.Default.Info,
                    color = CyberPurple,
                    title = "About VaultHide",
                    subtitle = "Version 1.0 • Mission and founder",
                    onClick = onNavigateAbout,
                    testTag = "setting_about"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Founder & CEO Section (Mandatory requirement)
        Text(
            text = "LEADERSHIP",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        FounderBadge(detailed = true)

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    color: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 13.dp)
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
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
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
