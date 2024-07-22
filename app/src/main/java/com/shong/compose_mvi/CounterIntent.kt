package com.shong.compose_mvi

data class CounterState(val count: Int = 0)

sealed class CounterIntent {
    object Increment : CounterIntent()
    object Decrement : CounterIntent()
}