package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldenAccent
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.GoldenYellowLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultSurface

@Composable
fun FounderBadge(
    modifier: Modifier = Modifier,
    detailed: Boolean = false
) {
    if (detailed) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            VaultSurface,
                            Color(0xFF131A2E)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            GoldenYellow.copy(alpha = 0.6f),
                            GoldenYellowLight.copy(alpha = 0.2f),
                            GoldenAccent.copy(alpha = 0.6f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .testTag("founder_ceo_card")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(GoldenYellow.copy(alpha = 0.25f), Color(0xFF3B2F0B))
                            ),
                            shape = CircleShape
                        )
                        .border(1.dp, GoldenYellow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "Verified Founder",
                        tint = GoldenYellow,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "FOUNDER & CEO",
                        color = GoldenYellowLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "अनंतान्य राजकुमार",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Founder & CEO — अनंतान्य राजकुमार",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }
    } else {
        // Compact pill badge
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF12192B))
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            GoldenYellow.copy(alpha = 0.5f),
                            GoldenAccent.copy(alpha = 0.7f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .testTag("founder_ceo_pill")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = GoldenYellow,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Founder & CEO — अनंतान्य राजकुमार",
                    color = GoldenYellowLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
