package com.shong.compose_mvi.ui.composable.counter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.shong.compose_mvi.ui.theme.Compose_MVITheme

@Composable
fun CounterView(counterViewModel: CounterViewModel = hiltViewModel()) {
    val state by counterViewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Count: ${state.count}")
        Button(onClick = { counterViewModel.setEvent(CounterEvent.Increment) }) {
            Text("Increment")
        }
        Button(onClick = { counterViewModel.setEvent(CounterEvent.Decrement) }) {
            Text("Decrement")
        }
    }
}

@Preview(backgroundColor = 0xffffffff)
@Composable
fun CounterPreview() {
    Compose_MVITheme {
        CounterView()
    }
}