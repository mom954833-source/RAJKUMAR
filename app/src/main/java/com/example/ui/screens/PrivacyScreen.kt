package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
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
fun PrivacyScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("privacy_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("privacy_back_btn")
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
                    text = "Privacy Policy",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("privacy_title")
                )
                Text(
                    text = "Transparent disclosures and user control",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Commitment Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = EmeraldSuccess.copy(alpha = 0.4f),
            backgroundColor = Color(0xFF10192A)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Policy, null, tint = EmeraldSuccess, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Honest Security Commitment",
                        color = EmeraldSuccess,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "VaultHide is built as a privacy-focused tool providing protected access and secure authentication. We never make unsubstantiated claims such as '100% unhackable' or 'impossible to break'. Our security model relies on standard, verified Android OS cryptographic primitives.",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section 1: What Information the App Accesses
        PrivacyDisclosureSection(
            icon = Icons.Default.Visibility,
            iconColor = ElectricBlue,
            title = "1. Information We Access",
            content = "VaultHide queries only installed launcher application labels and package names on your device using standard Android Intent queries (ACTION_MAIN, CATEGORY_LAUNCHER). We do NOT access any application's internal files, messages, call logs, contacts, photos, passwords, or personal credentials."
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Section 2: Why Permissions Are Required
        PrivacyDisclosureSection(
            icon = Icons.Default.Key,
            iconColor = CyberPurple,
            title = "2. Permissions & Hardware Access",
            content = "• USE_BIOMETRIC: Enables seamless authentication via fingerprint or face recognition using AndroidX BiometricPrompt.\n• VIBRATE: Provides subtle tactile haptic feedback during numeric keypad interaction.\nNo dangerous runtime permissions such as camera, microphone, contacts, SMS, or external storage are requested."
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Section 3: Data Stored Locally
        PrivacyDisclosureSection(
            icon = Icons.Default.Security,
            iconColor = GoldenYellow,
            title = "3. Local Data Storage",
            content = "All sensitive data is stored strictly on your local device. Your master PIN and password are never stored in plain text—only cryptographically salted SHA-256 hashes generated with SecureRandom are saved. The list of protected application package names is kept in an isolated local Room database."
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Section 4: Server Communication (Zero Bytes)
        PrivacyDisclosureSection(
            icon = Icons.Default.CloudOff,
            iconColor = EmeraldSuccess,
            title = "4. Zero External Server Transmission",
            content = "VaultHide operates completely offline. No telemetry, usage statistics, identifiers, or crash logs are transmitted to external cloud servers, advertisers, or third-party networks. Your private vault data remains solely on your physical device."
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Section 5: Deleting Your Information
        PrivacyDisclosureSection(
            icon = Icons.Default.DeleteForever,
            iconColor = Color(0xFFEF4444),
            title = "5. Deleting Your Information",
            content = "You maintain complete sovereignty over your data. You can erase all protected app configurations, cryptographic keys, master PIN, and security records instantly by using the 'Reset Vault' feature in Security Settings. Uninstalling the app also purges all application storage."
        )

        Spacer(modifier = Modifier.height(24.dp))

        FounderBadge(detailed = true)

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun PrivacyDisclosureSection(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    content: String
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(iconColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = iconColor, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = content,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}
