package com.shong.compose_mvi.presentation.screen.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shong.compose_mvi.presentation.component.BasicProgressBar
import com.shong.compose_mvi.presentation.screen.main.MainEvent
import com.shong.compose_mvi.presentation.screen.main.TimeState
import com.shong.compose_mvi.presentation.theme.Compose_MVITheme

@Composable
fun TimeView(state: TimeState, event: (MainEvent) -> Unit) {
//    var timeStr by remember { mutableStateOf("버튼을 누르세요") }
    Column(
        modifier = Modifier.background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val timeStr = if (state.isLoading) {
            ""
        } else {
            state.timeString.ifEmpty {
                state.error ?: "알 수 없는 에러"
            }
        }
        if (timeStr.isNotEmpty()) {
            Text(timeStr)
        } else {
            BasicProgressBar(modifier = Modifier.size(50.dp))
        }

        Button(onClick = { event(MainEvent.GetDeviceTime) }) {
            Text("Get Device Time")
        }
        Button(onClick = { event(MainEvent.GetInternetTime) }) {
            Text("Get Internet Time")
        }
    }
}

@Preview
@Composable
fun TimePreview() {
    Compose_MVITheme {
        TimeView(state = TimeState(), event = {})
    }
}
