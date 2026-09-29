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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GoldenAccent
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.GoldenYellowLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultBackground
import com.example.ui.theme.VaultSurface

@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("about_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("about_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "About VaultHide",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag("about_screen_title")
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Center Hero Logo & Title
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlowingShieldIcon(size = 90.dp)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "VaultHide",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                modifier = Modifier.testTag("about_brand_name")
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your Apps. Your Privacy. Your Control.",
                color = GoldenYellowLight,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("about_tagline")
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mission & Description Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "APPLICATION OVERVIEW",
                    color = ElectricBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "VaultHide is a privacy-focused Android application designed to help users protect access to compatible applications through a private vault experience.",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.testTag("about_description_text")
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Built strictly within legitimate Android framework APIs, VaultHide guarantees device-owner privacy without exploiting root, injecting malicious processes, or capturing personal data.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Prominent Founder & CEO Section (Mandatory Requirement)
        Text(
            text = "FOUNDER & CEO",
            color = GoldenYellow,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = GoldenYellow.copy(alpha = 0.5f),
            backgroundColor = Color(0xFF14192A)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(GoldenYellow.copy(alpha = 0.3f), Color(0xFF2E2207))
                                )
                            )
                            .border(1.5.dp, GoldenAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = GoldenYellow,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Founder & CEO",
                            color = GoldenYellowLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "अनंतान्य राजकुमार",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("founder_name_display")
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Founder & CEO — अनंतान्य राजकुमार",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF263353))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Led by अनंतान्य राजकुमार, VaultHide stands firmly on the principle of individual user sovereignty over device privacy.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Core Pillars
        Text(
            text = "SECURITY PILLARS",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                PillarRow(
                    icon = Icons.Default.Shield,
                    title = "Privacy-Focused",
                    description = "100% offline local processing. Zero telemetry sent to external servers."
                )
                Spacer(modifier = Modifier.height(12.dp))
                PillarRow(
                    icon = Icons.Default.Lock,
                    title = "Protected Access",
                    description = "Cryptographically salted authentication with AndroidX BiometricPrompt."
                )
                Spacer(modifier = Modifier.height(12.dp))
                PillarRow(
                    icon = Icons.Default.Security,
                    title = "Secure Authentication",
                    description = "Protected against brute force with progressive delay and cooldown."
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        FounderBadge(detailed = false)

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun PillarRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(ElectricBlue.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = ElectricBlue, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(description, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
}
