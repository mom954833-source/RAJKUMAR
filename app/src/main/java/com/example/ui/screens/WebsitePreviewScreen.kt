package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FounderBadge
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingShieldIcon
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DeepIndigo
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.GoldenYellowLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultBackground

@Composable
fun WebsitePreviewScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("website_preview_screen")
    ) {
        // Navigation bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("website_back_btn")
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
                        text = "VaultHide Official Website",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "vaulthide.app • Live Preview",
                        color = EmeraldSuccess,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF162038))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "SSL Secured",
                    color = ElectricBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Section
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = ElectricBlue.copy(alpha = 0.5f),
            backgroundColor = Color(0xFF0F1528)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GlowingShieldIcon(size = 80.dp)

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Keep Your Apps Private",
                    color = TextPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.testTag("web_hero_heading")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your Apps. Your Privacy. Your Control.",
                    color = GoldenYellowLight,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("web_hero_subheading")
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "VaultHide is a privacy-focused Android application designed to protect access to compatible apps through a private vault experience.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("web_get_vaulthide_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                    ) {
                        Text("Get VaultHide", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("web_learn_more_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Text("Learn More", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Features Section
        WebSectionHeader(title = "FEATURES", subtitle = "Engineered for personal device security")
        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            WebFeatureRow("Biometric Hardware Unlock", "Touch fingerprint sensor or face biometrics via AndroidX BiometricPrompt.")
            WebFeatureRow("Master 4-Digit PIN & Password", "Dual-tier salted cryptographic hashing with zero plain text storage.")
            WebFeatureRow("Auto-Lock Inactivity Triggers", "Locks instantly when screen turns off or when leaving app foreground.")
            WebFeatureRow("Legitimate OS Integration", "Respects Android platform bounds with zero exploits, malware, or rooting.")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Security Section
        WebSectionHeader(title = "SECURITY ARCHITECTURE", subtitle = "Cryptographic integrity by design")
        Spacer(modifier = Modifier.height(10.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "• SHA-256 with CSPRNG Salt (SecureRandom)\n• Local Isolated Room SQLite Storage\n• Cooldown protection against brute-force attempts\n• Zero third-party analytics or external network calls",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Supported Devices Section
        WebSectionHeader(title = "SUPPORTED DEVICES", subtitle = "Android 8.0 Oreo through Android 15 & beyond")
        Spacer(modifier = Modifier.height(10.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Smartphone, null, tint = ElectricBlue, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text("Compatible with All Android Devices", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Requires Android 8.0+ (API 24+) with biometric sensor or keypad support.", color = TextSecondary, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // About & Founder Section (Mandatory requirement)
        WebSectionHeader(title = "LEADERSHIP & ABOUT", subtitle = "Founder & CEO details")
        Spacer(modifier = Modifier.height(10.dp))

        FounderBadge(detailed = true)

        Spacer(modifier = Modifier.height(20.dp))

        // Contact Section
        WebSectionHeader(title = "CONTACT & SUPPORT", subtitle = "Official channels")
        Spacer(modifier = Modifier.height(10.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, null, tint = GoldenYellow, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("privacy@vaulthide.internal", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text("Dedicated support for device privacy and security inquiries.", color = TextSecondary, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "© 2026 VaultHide. All rights reserved.\nFounder & CEO — अनंतान्य राजकुमार",
            color = TextMuted,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun WebSectionHeader(title: String, subtitle: String) {
    Column {
        Text(title, color = ElectricBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Text(subtitle, color = TextSecondary, fontSize = 13.sp)
    }
}

@Composable
private fun WebFeatureRow(title: String, desc: String) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(EmeraldSuccess.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, null, tint = EmeraldSuccess, modifier = Modifier.size(14.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(desc, color = TextSecondary, fontSize = 12.sp)
            }
        }
    }
}
