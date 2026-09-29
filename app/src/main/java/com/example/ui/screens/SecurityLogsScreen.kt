package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SecurityLogEntity
import com.example.ui.components.FounderBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultBackground
import com.example.ui.theme.VaultSurface
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SecurityLogsScreen(
    logs: List<SecurityLogEntity>,
    onBack: () -> Unit,
    onClearLogs: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var showClearDialog by remember { mutableStateOf(false) }

    val filterOptions = listOf("All", "Successful Unlocks", "Failed Attempts", "Credential Changes")

    val filteredLogs = remember(logs, selectedFilter) {
        when (selectedFilter) {
            "Successful Unlocks" -> logs.filter { it.isSuccess && (it.eventType == "VAULT_UNLOCKED") }
            "Failed Attempts" -> logs.filter { !it.isSuccess }
            "Credential Changes" -> logs.filter {
                it.eventType.contains("PIN") || it.eventType.contains("PASSWORD") || it.eventType.contains("INITIALIZED")
            }
            else -> logs
        }
    }

    val totalCount = logs.size
    val successCount = logs.count { it.isSuccess && it.eventType == "VAULT_UNLOCKED" }
    val failedCount = logs.count { !it.isSuccess }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBackground)
            .padding(16.dp)
            .testTag("security_logs_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("security_logs_back_btn")
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
                        text = "Security Logs",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("security_logs_title")
                    )
                    Text(
                        text = "Local Room database audit trail",
                        color = ElectricBlue,
                        fontSize = 12.sp
                    )
                }
            }

            if (logs.isNotEmpty()) {
                IconButton(
                    onClick = { showClearDialog = true },
                    modifier = Modifier.testTag("clear_logs_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear Logs",
                        tint = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Summary Metric Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryBox(
                modifier = Modifier.weight(1f),
                title = "Total Events",
                value = "$totalCount",
                color = ElectricBlue
            )
            SummaryBox(
                modifier = Modifier.weight(1f),
                title = "Successful",
                value = "$successCount",
                color = EmeraldSuccess
            )
            SummaryBox(
                modifier = Modifier.weight(1f),
                title = "Failed",
                value = "$failedCount",
                color = if (failedCount > 0) CrimsonDanger else TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Filter chips row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filterOptions) { filter ->
                val isSelected = filter == selectedFilter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) ElectricBlue else Color(0xFF131A2E))
                        .border(
                            1.dp,
                            if (isSelected) ElectricBlue else Color(0xFF263353),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("filter_chip_$filter")
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.White else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Log Items List
        if (filteredLogs.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF131A2E))
                            .border(1.dp, Color(0xFF263353), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "No Security Logs Found",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (logs.isEmpty()) {
                            "Authentication attempts and security events will be securely recorded in your local Room database."
                        } else {
                            "No events matching the selected filter."
                        },
                        color = TextSecondary,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("security_logs_list"),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(filteredLogs, key = { it.id }) { log ->
                    SecurityLogCard(log = log)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        FounderBadge(detailed = false)
    }

    // Confirmation Dialog to Clear Logs
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            containerColor = VaultSurface,
            title = {
                Text("Clear Security Logs?", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Are you sure you want to delete all historical security events from your local database? This cannot be undone.",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearLogs()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonDanger)
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun SummaryBox(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    color: Color
) {
    GlassCard(
        modifier = modifier,
        cornerRadius = 14.dp,
        borderColor = color.copy(alpha = 0.35f),
        backgroundColor = Color(0xFF11172A)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = color,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                color = TextSecondary,
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SecurityLogCard(log: SecurityLogEntity) {
    val (icon, iconColor, title) = when (log.eventType) {
        "VAULT_UNLOCKED" -> Triple(Icons.Default.LockOpen, EmeraldSuccess, "Vault Unlocked")
        "FAILED_ATTEMPT" -> Triple(Icons.Default.Error, CrimsonDanger, "Failed Authentication")
        "VAULT_INITIALIZED" -> Triple(Icons.Default.Security, ElectricBlue, "Vault Initialized")
        "PIN_CHANGED" -> Triple(Icons.Default.Key, CyberPurple, "PIN Changed")
        "PASSWORD_CHANGED" -> Triple(Icons.Default.Lock, CyberPurple, "Password Changed")
        "APP_LAUNCHED" -> Triple(Icons.Default.Launch, ElectricBlue, "Protected App Launched")
        "APP_UNPROTECTED" -> Triple(Icons.Default.Shield, GoldenYellow, "App Removed from Vault")
        else -> Triple(
            if (log.isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
            if (log.isSuccess) EmeraldSuccess else CrimsonDanger,
            log.eventType.replace('_', ' ')
        )
    }

    val formattedDate = remember(log.timestamp) {
        val sdf = SimpleDateFormat("MMM d, yyyy • hh:mm a", Locale.getDefault())
        sdf.format(Date(log.timestamp))
    }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("security_log_item_${log.id}"),
        cornerRadius = 14.dp,
        borderColor = if (!log.isSuccess) CrimsonDanger.copy(alpha = 0.45f) else Color(0xFF202A44),
        backgroundColor = if (!log.isSuccess) Color(0xFF1B1118) else Color(0xFF11172A)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconColor.copy(alpha = 0.15f))
                        .border(1.dp, iconColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = log.description,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedDate,
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (log.isSuccess) EmeraldSuccess.copy(alpha = 0.15f) else CrimsonDanger.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (log.isSuccess) "Success" else "Blocked",
                    color = if (log.isSuccess) EmeraldSuccess else CrimsonDanger,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
