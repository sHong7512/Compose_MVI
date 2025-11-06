package com.shong.compose_mvi.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
fun LaunchedFadeIn(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1000,
    content: @Composable (() -> Unit)? = null,
) {
    var alphaAnimState by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (alphaAnimState) 1f else 0.3f,
        animationSpec = tween(durationMillis = durationMillis),
        label = "FadeAnim",
    )
    LaunchedEffect(key1 = Unit) { alphaAnimState = true }
    Box(modifier = modifier.alpha(alpha)) {
        content?.invoke()
    }
}