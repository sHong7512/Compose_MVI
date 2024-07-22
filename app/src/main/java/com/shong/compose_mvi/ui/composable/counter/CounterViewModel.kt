package com.shong.compose_mvi.ui.composable.counter

import com.shong.compose_mvi.ui.composable.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor() :
    BaseViewModel<CounterEvent, CounterState, CounterEffect>() {
    override fun createInitialState(): CounterState {
        return CounterState(count = 0)
    }

    override fun handleEvent(event: CounterEvent) {
        val newState: CounterState = when (event) {
            is CounterEvent.Increment -> {
                currentState.copy(count = currentState.count + 1)
            }

            is CounterEvent.Decrement -> {
                currentState.copy(count = currentState.count - 1)
            }
        }
        setState(newState)
    }
}