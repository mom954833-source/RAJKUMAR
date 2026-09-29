package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultSurfaceVariant
import com.example.util.HapticFeedbackManager
import kotlin.math.roundToInt

@Composable
fun PinDotsIndicator(
    pinLength: Int,
    maxDigits: Int = 4,
    hasError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(hasError) {
        if (hasError) {
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 350
                    0f at 0
                    -16f at 50
                    16f at 100
                    -12f at 150
                    12f at 200
                    -6f at 250
                    6f at 300
                    0f at 350
                }
            )
        }
    }

    Row(
        modifier = modifier
            .padding(vertical = 16.dp)
            .offset { IntOffset(shakeOffset.value.roundToInt(), 0) },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until maxDigits) {
            val isFilled = i < pinLength
            val dotSize by animateDpAsState(
                targetValue = if (isFilled) 16.dp else 12.dp,
                animationSpec = tween(150),
                label = "dot_size"
            )
            val dotColor by animateColorAsState(
                targetValue = when {
                    hasError -> CrimsonDanger
                    isFilled -> ElectricBlue
                    else -> VaultSurfaceVariant
                },
                animationSpec = tween(150),
                label = "dot_color"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(dotColor)
                    .border(
                        width = 1.5.dp,
                        color = if (hasError) CrimsonDanger else if (isFilled) ElectricBlue else Color(0xFF334155),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun PinPad(
    modifier: Modifier = Modifier,
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onBiometricClick: (() -> Unit)? = null,
    showBiometric: Boolean = true
) {
    val context = LocalContext.current

    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("BIO", "0", "DEL")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (item in row) {
                    when (item) {
                        "BIO" -> {
                            if (showBiometric && onBiometricClick != null) {
                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF131A2E))
                                        .border(1.dp, CyberPurple.copy(alpha = 0.4f), CircleShape)
                                        .clickable {
                                            HapticFeedbackManager.performKeypressFeedback(context)
                                            onBiometricClick()
                                        }
                                        .testTag("pin_pad_biometric"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Fingerprint,
                                        contentDescription = "Use Biometric",
                                        tint = ElectricBlue,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(68.dp))
                            }
                        }
                        "DEL" -> {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF131A2E))
                                    .border(1.dp, Color(0xFF263353), CircleShape)
                                    .clickable {
                                        HapticFeedbackManager.performKeypressFeedback(context)
                                        onDeleteClick()
                                    }
                                    .testTag("pin_pad_delete"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                                    contentDescription = "Delete",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        else -> {
                            // Numeric Key
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF141B2D))
                                    .border(1.dp, Color(0xFF232D48), CircleShape)
                                    .clickable {
                                        HapticFeedbackManager.performKeypressFeedback(context)
                                        onDigitClick(item)
                                    }
                                    .testTag("pin_key_$item"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item,
                                    color = TextPrimary,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
