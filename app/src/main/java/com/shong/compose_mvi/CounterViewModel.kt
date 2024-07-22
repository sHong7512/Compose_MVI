package com.shong.compose_mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CounterViewModel : ViewModel() {
    private val _state = MutableStateFlow(CounterState())
    val state: StateFlow<CounterState> = _state.asStateFlow()

    fun processIntent(intent: CounterIntent) {
        when (intent) {
            is CounterIntent.Increment -> _state.value =
                _state.value.copy(count = _state.value.count + 1)

            is CounterIntent.Decrement -> _state.value =
                _state.value.copy(count = _state.value.count - 1)
        }
    }
}