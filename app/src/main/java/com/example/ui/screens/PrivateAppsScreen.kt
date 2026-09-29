package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProtectedAppEntity
import com.example.ui.components.FounderBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultBackground
import com.example.ui.theme.VaultSurface

@Composable
fun PrivateAppsScreen(
    protectedApps: List<ProtectedAppEntity>,
    onBack: () -> Unit,
    onNavigateAddApp: () -> Unit,
    onOpenApp: (packageName: String, appName: String) -> Unit,
    onRemoveApp: (packageName: String, appName: String) -> Unit
) {
    var appToRemove by remember { mutableStateOf<ProtectedAppEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBackground)
            .padding(16.dp)
            .testTag("private_apps_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("private_apps_back_btn")
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
                        text = "My Private Apps",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("my_private_apps_title")
                    )
                    Text(
                        text = "${protectedApps.size} apps protected in vault",
                        color = ElectricBlue,
                        fontSize = 12.sp
                    )
                }
            }

            Button(
                onClick = onNavigateAddApp,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                modifier = Modifier.testTag("add_app_header_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add App", fontSize = 13.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (protectedApps.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF131A2E))
                            .border(1.dp, ElectricBlue.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = ElectricBlue,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "No Protected Apps Yet",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Add messaging, social, or private apps to protect them behind your biometric/PIN vault.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onNavigateAddApp,
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("empty_protect_first_app_btn")
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Protect Your First App")
                    }
                }
            }
        } else {
            // Grid of Protected Apps
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .testTag("protected_apps_grid"),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(protectedApps, key = { it.packageName }) { app ->
                    ProtectedAppCard(
                        app = app,
                        onOpen = { onOpenApp(app.packageName, app.appName) },
                        onRemove = { appToRemove = app }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        FounderBadge(detailed = false)
    }

    // Confirmation Dialog for App Removal
    if (appToRemove != null) {
        val app = appToRemove!!
        AlertDialog(
            onDismissRequest = { appToRemove = null },
            containerColor = VaultSurface,
            title = {
                Text("Remove from Vault?", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Are you sure you want to remove ${app.appName} from your private vault? It will no longer require authentication.",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRemoveApp(app.packageName, app.appName)
                        appToRemove = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonDanger)
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { appToRemove = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun ProtectedAppCard(
    app: ProtectedAppEntity,
    onOpen: () -> Unit,
    onRemove: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("protected_app_card_${app.packageName}"),
        cornerRadius = 18.dp,
        borderColor = ElectricBlue.copy(alpha = 0.35f),
        backgroundColor = Color(0xFF12182B)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Avatar / Icon Placeholder
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            listOf(ElectricBlue.copy(alpha = 0.25f), CyberPurple.copy(alpha = 0.25f))
                        )
                    )
                    .border(1.dp, ElectricBlue.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = app.appName.take(1).uppercase(),
                    color = ElectricBlue,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // App Name
            Text(
                text = app.appName,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 🔒 Protected Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🔒 Protected",
                    color = EmeraldSuccess,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Open & Remove
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = onOpen,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .testTag("open_app_btn_${app.packageName}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Open", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .testTag("remove_app_btn_${app.packageName}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonDanger),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonDanger.copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Remove", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
