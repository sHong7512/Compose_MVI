package com.shong.compose_mvi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(counterViewModel: CounterViewModel = viewModel()) {
    val state by counterViewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Count: ${state.count}")
        Button(onClick = { counterViewModel.processIntent(CounterIntent.Increment) }) {
            Text("Increment")
        }
        Button(onClick = { counterViewModel.processIntent(CounterIntent.Decrement) }) {
            Text("Decrement")
        }
    }
}