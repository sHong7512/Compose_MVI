package com.shong.compose_mvi.presentation.screen.main.view

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shong.compose_mvi.presentation.component.BasicProgressBar
import com.shong.compose_mvi.presentation.screen.main.LogState
import com.shong.compose_mvi.presentation.screen.main.MainEvent
import com.shong.compose_mvi.presentation.theme.Compose_MVITheme
import com.shong.compose_mvi.presentation.theme.Pink80

@Composable
fun LogView(state: LogState, event: (MainEvent) -> Unit) {
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
            if (state.isLoading) {
                BasicProgressBar(modifier = Modifier.fillMaxSize())
            } else {
                if (state.logs.isEmpty()) {
                    Text("로그가 없습니다.", color = Color.White)
                } else {
                    LazyColumn {
                        items(state.logs.size) { index ->
                            Item(
                                state.logs[index].first,
                                state.logs[index].second
                            )
                        }
                    }
                }
            }
        }

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
                    event(MainEvent.AddLog(buf))

                },
            ) {
                Text("Add Log")
            }
            Button(
                modifier = Modifier.weight(0.3f),
                onClick = { event(MainEvent.RemoveLogs) }) {
                Text("Clear")
            }
        }
    }
}

@Preview
@Composable
fun LogPreview() {
    Compose_MVITheme {
        LogView(
            state = LogState(),
            event = {}
        )
    }
}

@Composable
private fun Item(msg: String, korTime: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(color = Color.White),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = "msg::$msg")
        Text(text = "time::$korTime")
    }
}