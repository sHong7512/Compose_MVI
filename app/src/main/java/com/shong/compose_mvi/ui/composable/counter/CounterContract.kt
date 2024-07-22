package com.shong.compose_mvi.ui.composable.counter

import com.shong.compose_mvi.ui.composable.UiEffect
import com.shong.compose_mvi.ui.composable.UiEvent
import com.shong.compose_mvi.ui.composable.UiState

sealed class CounterEvent : UiEvent {
    data object Increment : CounterEvent()
    data object Decrement : CounterEvent()
}

data class CounterState(val count: Int = 0) : UiState

sealed class CounterEffect : UiEffect {
    data object ShowToast : CounterEffect()
}