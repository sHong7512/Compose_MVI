package com.shong.compose_mvi.presentation.screen.main.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.shong.compose_mvi.presentation.screen.main.CounterState
import com.shong.compose_mvi.presentation.screen.main.MainEvent
import com.shong.compose_mvi.presentation.theme.Compose_MVITheme

@Composable
fun CounterView(state: CounterState, event: (MainEvent) -> Unit) {
//    var count by remember { mutableStateOf(0) }
//    count = state.count

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Count: ${state.count}")
        Button(onClick = {
            event.invoke(MainEvent.Increment)
        }) {
            Text("Increment")
        }
        Button(onClick = {
            event.invoke(MainEvent.Decrement)
        }) {
            Text("Decrement")
        }
    }
}

@Preview(backgroundColor = 0xffffffff)
@Composable
fun CounterPreview() {
    Compose_MVITheme {
        CounterView(
            state = CounterState(),
            event = {}
        )
    }
}