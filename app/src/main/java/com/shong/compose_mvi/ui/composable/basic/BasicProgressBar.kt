package com.shong.compose_mvi.ui.composable.basic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shong.compose_mvi.ui.theme.Compose_MVITheme


@Composable
fun BasicProgressBar(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp)
        )
    }
}

@Preview
@Composable
fun BasicProgressBarPreview() {
    Compose_MVITheme {
        BasicProgressBar(
            modifier = Modifier.fillMaxSize()
        )
    }
}