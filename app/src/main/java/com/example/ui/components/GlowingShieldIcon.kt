package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue

@Composable
fun GlowingShieldIcon(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    animated: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shield_pulse")
    val pulseScale by if (animated) {
        infiniteTransition.animateFloat(
            initialValue = 0.95f,
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(2400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    }

    Box(
        modifier = modifier.size(size * 1.3f),
        contentAlignment = Alignment.Center
    ) {
        // Glowing aura behind shield
        Box(
            modifier = Modifier
                .size(size)
                .scale(pulseScale)
                .blur(20.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            CyberPurple.copy(alpha = 0.45f),
                            ElectricBlue.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // The VaultHide Shield Logo
        Image(
            painter = painterResource(id = R.drawable.ic_vaulthide_logo),
            contentDescription = "VaultHide Logo",
            modifier = Modifier.size(size)
        )
    }
}
