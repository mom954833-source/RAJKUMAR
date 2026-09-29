package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VaultSurface

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    borderColor: Color = GlassBorder,
    backgroundColor: Color = VaultSurface,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val cardModifier = if (onClick != null) {
        modifier
            .fillMaxWidth()
            .clip(shape)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        borderColor.copy(alpha = 0.5f),
                        borderColor.copy(alpha = 0.15f)
                    )
                ),
                shape = shape
            )
            .background(backgroundColor)
            .clickable { onClick() }
    } else {
        modifier
            .fillMaxWidth()
            .clip(shape)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        borderColor.copy(alpha = 0.5f),
                        borderColor.copy(alpha = 0.15f)
                    )
                ),
                shape = shape
            )
            .background(backgroundColor)
    }

    Box(modifier = cardModifier, content = content)
}
