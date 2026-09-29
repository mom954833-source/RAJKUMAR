package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InstalledAppInfo
import com.example.ui.components.FounderBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.GoldenYellowLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultBackground
import com.example.ui.theme.VaultSurface

@Composable
fun AddAppScreen(
    deviceApps: List<InstalledAppInfo>,
    selectedPackages: Set<String>,
    alreadyProtectedPackages: Set<String>,
    searchQuery: String,
    categoryFilter: String,
    isLoading: Boolean,
    onBack: () -> Unit,
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onToggleSelect: (String) -> Unit,
    onSaveToVault: () -> Unit
) {
    val categories = listOf("All", "Messaging", "Social", "Finance", "Media", "General")

    val filteredApps = remember(deviceApps, searchQuery, categoryFilter, alreadyProtectedPackages) {
        deviceApps.filter { app ->
            val matchesSearch = app.appName.contains(searchQuery, ignoreCase = true) ||
                    app.packageName.contains(searchQuery, ignoreCase = true)
            val matchesCategory = (categoryFilter == "All" || app.category.equals(categoryFilter, ignoreCase = true))
            matchesSearch && matchesCategory
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBackground)
            .padding(16.dp)
            .testTag("add_app_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("add_app_back_btn")
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
                    text = "Add Private App",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("add_private_app_title")
                )
                Text(
                    text = "Select installed compatible apps to protect",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Legitimate Android Protection Transparency Banner (Mandatory Prompt Text)
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = GoldenYellow.copy(alpha = 0.4f),
            backgroundColor = Color(0xFF14192B)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = GoldenYellow,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "App Protection Notice",
                        color = GoldenYellowLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "App hiding is not supported on this device. Protect access through VaultHide where supported.",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "VaultHide never accesses other applications' private data, passwords, messages, photos or personal information.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search installed applications...") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = ElectricBlue) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("add_app_search_field"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricBlue,
                unfocusedBorderColor = Color(0xFF263353),
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = VaultSurface,
                unfocusedContainerColor = VaultSurface
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSelected = (cat == categoryFilter)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) ElectricBlue else Color(0xFF131A2E))
                        .border(
                            1.dp,
                            if (isSelected) ElectricBlue else Color(0xFF263353),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { onCategoryChange(cat) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("filter_chip_$cat")
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.White else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Apps List
        if (isLoading) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ElectricBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("apps_discovery_list"),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(filteredApps, key = { it.packageName }) { app ->
                    val isAlreadyProtected = alreadyProtectedPackages.contains(app.packageName)
                    val isSelected = selectedPackages.contains(app.packageName)

                    AppSelectionRow(
                        app = app,
                        isSelected = isSelected,
                        isAlreadyProtected = isAlreadyProtected,
                        onToggle = {
                            if (!isAlreadyProtected) {
                                onToggleSelect(app.packageName)
                            }
                        }
                    )
                }
            }
        }

        // Bottom Bar with Save Button
        if (selectedPackages.isNotEmpty()) {
            Button(
                onClick = onSaveToVault,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_selected_apps_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
            ) {
                Icon(Icons.Default.Check, null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Protect ${selectedPackages.size} Selected Apps",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        FounderBadge(detailed = false)
    }
}

@Composable
private fun AppSelectionRow(
    app: InstalledAppInfo,
    isSelected: Boolean,
    isAlreadyProtected: Boolean,
    onToggle: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("app_select_row_${app.packageName}"),
        cornerRadius = 14.dp,
        borderColor = if (isSelected) ElectricBlue else Color(0xFF202A44),
        backgroundColor = if (isSelected) Color(0xFF131D38) else Color(0xFF11172A),
        onClick = onToggle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // App Icon Placeholder
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                listOf(ElectricBlue.copy(alpha = 0.25f), CyberPurple.copy(alpha = 0.25f))
                            )
                        )
                        .border(1.dp, ElectricBlue.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
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
                        text = if (isAlreadyProtected) "🔒 Already in Vault" else app.category,
                        color = if (isAlreadyProtected) EmeraldSuccess else TextMuted,
                        fontSize = 12.sp
                    )
                }
            }

            if (isAlreadyProtected) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(EmeraldSuccess.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Vaulted",
                        color = EmeraldSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = ElectricBlue,
                        uncheckedColor = TextSecondary,
                        checkmarkColor = Color.White
                    ),
                    modifier = Modifier.testTag("checkbox_${app.packageName}")
                )
            }
        }
    }
}
