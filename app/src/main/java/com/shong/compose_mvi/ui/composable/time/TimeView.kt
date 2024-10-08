package com.shong.compose_mvi.ui.composable.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shong.compose_mvi.data.DummyData
import com.shong.compose_mvi.data.remote.model.TimeResponse
import com.shong.compose_mvi.ui.composable.common.BasicProgressBar
import com.shong.compose_mvi.ui.theme.Compose_MVITheme
import com.shong.compose_mvi.util.DateFormatter

@Composable
fun TimeView(viewModel: TimeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    Content(state = state, event = viewModel::handleEvent)
}

@Preview
@Composable
fun TimePreview() {
    Compose_MVITheme {
        val unixTimestamp = DummyData.get<TimeResponse>(LocalContext.current).unixTime
        Content(state = TimeState.Success(dateStr = DateFormatter().format(unixTimestamp))) {}
    }
}

/*
// DI 된거는 일반적인 방식과 다르게 프리뷰 가져와야함. 비교용으로 남겨둠
@Preview
@Composable
private fun TimePreview_Worse() {
    Compose_MVITheme {
        TimeView()
    }
}
 */

@Composable
private fun Content(
    state: TimeState,
    event: (TimeEvent) -> Unit,
) {
    Column(
        modifier = Modifier.background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is TimeState.Success -> Text(state.dateStr)
            is TimeState.Loading -> BasicProgressBar(modifier = Modifier.size(50.dp))
            is TimeState.Fail -> Text(state.error)
            is TimeState.Initial -> Text("버튼을 누르세요")
        }
        Button(onClick = { event(TimeEvent.GetDeviceTime) }) {
            Text("Get Device Time")
        }
        Button(onClick = { event(TimeEvent.GetInternetTime) }) {
            Text("Get Internet Time")
        }
    }
}

