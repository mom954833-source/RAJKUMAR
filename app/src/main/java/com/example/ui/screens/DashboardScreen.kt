package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProtectedAppEntity
import com.example.ui.components.FounderBadge
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingShieldIcon
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldenAccent
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.GoldenYellowLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultBackground
import com.example.ui.theme.VaultSurface
import com.example.ui.theme.VaultSurfaceVariant

@Composable
fun DashboardScreen(
    protectedCount: Int,
    recentProtectedApps: List<ProtectedAppEntity>,
    onNavigateMyApps: () -> Unit,
    onNavigateAddApp: () -> Unit,
    onNavigateSecurity: () -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateWebsite: () -> Unit,
    onNavigateLogs: () -> Unit,
    onLockVault: () -> Unit,
    onLaunchApp: (packageName: String, appName: String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("dashboard_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                GlowingShieldIcon(size = 42.dp, animated = false)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "VaultHide",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Your Apps. Your Privacy. Your Control.",
                        color = GoldenYellowLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(
                onClick = onLockVault,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(VaultSurfaceVariant)
                    .testTag("dashboard_lock_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Lock Vault",
                    tint = GoldenYellow
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Welcome Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = ElectricBlue.copy(alpha = 0.4f),
            backgroundColor = Color(0xFF0F1528)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Welcome to VaultHide",
                            color = TextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("dashboard_welcome_title")
                        )
                        Text(
                            text = "Private App Protection Active",
                            color = EmeraldSuccess,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(EmeraldSuccess.copy(alpha = 0.15f))
                            .border(1.dp, EmeraldSuccess, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "All selected applications are encrypted and guarded by your master authentication key.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Statistics Row
        Text(
            text = "STATISTICS & STATUS",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Protected Apps",
                value = "$protectedCount",
                status = if (protectedCount > 0) "Active" else "Empty",
                accentColor = ElectricBlue,
                icon = Icons.Default.Apps,
                testTag = "stat_protected_apps"
            )

            StatCard(
                modifier = Modifier.weight(1f),
                title = "Security Status",
                value = "High",
                status = "View Logs →",
                accentColor = EmeraldSuccess,
                icon = Icons.Default.VerifiedUser,
                testTag = "stat_security_status",
                onClick = onNavigateLogs
            )

            StatCard(
                modifier = Modifier.weight(1f),
                title = "Vault Status",
                value = "Encrypted",
                status = "Auto-Lock On",
                accentColor = GoldenYellow,
                icon = Icons.Default.Lock,
                testTag = "stat_vault_status"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Main Navigation Action Buttons
        Text(
            text = "VAULT ACTIONS",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionButton(
                modifier = Modifier.weight(1f),
                title = "My Private Apps",
                subtitle = "$protectedCount apps vaulted",
                icon = Icons.Default.Apps,
                primaryColor = ElectricBlue,
                onClick = onNavigateMyApps,
                testTag = "btn_my_private_apps"
            )

            ActionButton(
                modifier = Modifier.weight(1f),
                title = "+ Add App",
                subtitle = "Protect new apps",
                icon = Icons.Default.Add,
                primaryColor = CyberPurple,
                onClick = onNavigateAddApp,
                testTag = "btn_add_app"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionButton(
                modifier = Modifier.weight(1f),
                title = "Security",
                subtitle = "Biometrics & PIN",
                icon = Icons.Default.Security,
                primaryColor = GoldenYellow,
                onClick = onNavigateSecurity,
                testTag = "btn_security"
            )

            ActionButton(
                modifier = Modifier.weight(1f),
                title = "Settings",
                subtitle = "Vault configuration",
                icon = Icons.Default.Settings,
                primaryColor = Color(0xFF64748B),
                onClick = onNavigateSettings,
                testTag = "btn_settings"
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Public App & Website Banner
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = ElectricBlue.copy(alpha = 0.45f),
            backgroundColor = Color(0xFF0F172E),
            onClick = onNavigateWebsite
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ElectricBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = ElectricBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "VaultHide Public Website",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "vaulthide.app • Public overview & features",
                            color = GoldenYellowLight,
                            fontSize = 12.sp
                        )
                    }
                }

                Button(
                    onClick = onNavigateWebsite,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    modifier = Modifier.testTag("btn_public_website_open")
                ) {
                    Text("Visit", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Recent Protected Apps Quick Access
        if (recentProtectedApps.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENT PROTECTED APPS",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "View All",
                    color = ElectricBlue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateMyApps() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                recentProtectedApps.take(4).forEach { app ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onLaunchApp(app.packageName, app.appName) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF1E293B)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = app.appName.take(1).uppercase(),
                                        color = ElectricBlue,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = app.appName,
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "🔒 Protected • ${app.category}",
                                        color = EmeraldSuccess,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Button(
                                onClick = { onLaunchApp(app.packageName, app.appName) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("Open", fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Founder & CEO Badge (Mandatory requirement)
        FounderBadge(detailed = true)

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    status: String,
    accentColor: Color,
    icon: ImageVector,
    testTag: String,
    onClick: (() -> Unit)? = null
) {
    GlassCard(
        modifier = modifier.testTag(testTag),
        cornerRadius = 16.dp,
        borderColor = accentColor.copy(alpha = 0.35f),
        backgroundColor = Color(0xFF11172A),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                color = TextSecondary,
                fontSize = 11.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = status,
                color = accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    primaryColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    GlassCard(
        modifier = modifier.testTag(testTag),
        cornerRadius = 16.dp,
        borderColor = primaryColor.copy(alpha = 0.4f),
        backgroundColor = Color(0xFF131A2E),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(primaryColor.copy(alpha = 0.2f))
                    .border(1.dp, primaryColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}
