package com.shong.compose_mvi.ui.composable.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shong.compose_mvi.data.db.AppLog
import com.shong.compose_mvi.ui.composable.basic.BasicProgressBar
import com.shong.compose_mvi.ui.theme.Compose_MVITheme
import com.shong.compose_mvi.ui.theme.Pink80

@Composable
fun LogView(viewModel: LogViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
//    viewModel.initialLogs()
    Content(state = state, event = viewModel::handleEvent)
}

@Preview
@Composable
fun LogPreview() {
    Compose_MVITheme {
        Content(
            state = LogState.Success(
                listOf(
                    AppLog(msg = "msg", micros = 100000000L),
                    AppLog(msg = "msg", micros = 100000000L),
                )
            )
        ) {}
    }
}

@Composable
private fun Content(
    state: LogState,
    event: (LogEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = Pink80)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Button(onClick = {
                event(LogEvent.AddLog("msg"))
            }) {
                Text("Add Log DB")
            }
            Button(onClick = { event(LogEvent.ClearLogs) }) {
                Text("Clear Logs")
            }
        }

        when (state) {
            is LogState.Success -> {
                if (state.data.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .background(color = Color.Black)
                            .padding(8.dp)
                    ) {
                        items(state.data.size) { index ->
                            Item(state.data[index])
                        }
                    }
                } else {
                    Text("로그가 없습니다.")
                }
            }

            is LogState.Fail -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.msg)
                }
            }

            LogState.Loading -> BasicProgressBar(
                modifier = Modifier
                    .fillMaxSize()
            )

            LogState.Init -> {}
        }
    }
}

@Composable
private fun Item(log: AppLog) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(color = Color.White),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = log.msg)
        Text(text = log.micros.toString())
    }
}