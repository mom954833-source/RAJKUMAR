package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FounderBadge
import com.example.ui.components.GlowingShieldIcon
import com.example.ui.theme.GoldenYellowLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VaultBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }
    var showFounder by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showContent = true
        delay(400)
        showTagline = true
        delay(400)
        showFounder = true
        delay(1600)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        VaultBackground,
                        Color(0xFF0F1424),
                        VaultBackground
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { -40 }
            ) {
                GlowingShieldIcon(size = 110.dp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600))
            ) {
                Text(
                    text = "VaultHide",
                    color = TextPrimary,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.testTag("splash_app_title")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            AnimatedVisibility(
                visible = showTagline,
                enter = fadeIn(tween(500))
            ) {
                Text(
                    text = "Your Apps. Your Privacy. Your Control.",
                    color = GoldenYellowLight,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("splash_tagline")
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            AnimatedVisibility(
                visible = showFounder,
                enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { 30 }
            ) {
                FounderBadge(detailed = true)
            }
        }
    }
}
