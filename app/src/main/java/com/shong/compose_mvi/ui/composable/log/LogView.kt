package com.shong.compose_mvi.ui.composable.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shong.compose_mvi.data.db.AppLog
import com.shong.compose_mvi.ui.composable.common.BasicProgressBar
import com.shong.compose_mvi.ui.theme.Compose_MVITheme
import com.shong.compose_mvi.ui.theme.Pink80
import com.shong.compose_mvi.util.DateFormatter

@Composable
fun LogView(viewModel: LogViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    Content(state = state, event = viewModel::handleEvent)
}

@Preview
@Composable
fun LogPreview() {
    Compose_MVITheme {
        Content(
            state = LogState.Success(
                KorTimeLog.convert(
                    listOf(
                        AppLog(msg = "msg", micros = 100000000L),
                        AppLog(msg = "msg", micros = 100000000L),
                    ), DateFormatter()
                )
            )
        ) {}
    }
}

@Composable
private fun Content(state: LogState, event: (LogEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = Pink80)
            .imePadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.Black)
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is LogState.Success -> {
                    if (state.data.isEmpty()) {
                        Text("로그가 없습니다.", color = Color.White)
                    } else {
                        LazyColumn { items(state.data.size) { index -> Item(state.data[index]) } }
                    }
                }

                is LogState.Fail -> {
                    Text(state.msg)
                }

                LogState.Loading -> BasicProgressBar(modifier = Modifier.fillMaxSize())

                LogState.Init -> {}
            }
        }

        UserUI(event = event)
    }
}

@Composable
private fun UserUI(event: (LogEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        var text by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.weight(0.4f),
            value = text,
            onValueChange = { text = it },
            shape = RoundedCornerShape(10.dp),
            placeholder = { Text("write message") }
        )
        Button(
            modifier = Modifier.weight(0.3f),
            onClick = {
                val buf = text
                text = ""
                event(LogEvent.AddLog(buf))

            },
        ) {
            Text("Add Log")
        }
        Button(
            modifier = Modifier.weight(0.3f),
            onClick = { event(LogEvent.ClearLogs) }) {
            Text("Clear")
        }
    }
}

@Composable
private fun Item(log: KorTimeLog) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(color = Color.White),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = "msg::${log.msg}")
        Text(text = "time::${log.korTime}")
    }
}