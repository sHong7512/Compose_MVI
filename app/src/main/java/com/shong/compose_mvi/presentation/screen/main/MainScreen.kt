package com.shong.compose_mvi.presentation.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shong.compose_mvi.presentation.screen.main.view.CounterView
import com.shong.compose_mvi.presentation.screen.main.view.LogView
import com.shong.compose_mvi.presentation.screen.main.view.TimeView

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState().value
    val event: (MainEvent) -> Unit = { viewModel.handleEvent(it) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            CounterView(state = state.counterState, event = event)
            TimeView(state = state.timeState, event = event)
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LogView(state = state.logState, event = event)
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    MainScreen()
}