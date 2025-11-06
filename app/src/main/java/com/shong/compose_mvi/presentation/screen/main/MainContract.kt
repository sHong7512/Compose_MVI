package com.shong.compose_mvi.presentation.screen.main

import com.shong.compose_mvi.presentation.screen.UiEffect
import com.shong.compose_mvi.presentation.screen.UiEvent
import com.shong.compose_mvi.presentation.screen.UiState

sealed class MainEvent : UiEvent {
    data object Increment : MainEvent()
    data object Decrement : MainEvent()
    data class AddLog(val msg: String) : MainEvent()
    data object RemoveLogs : MainEvent()
    data object GetInternetTime : MainEvent()
    data object GetDeviceTime : MainEvent()
}

// 스크린의 전체 상태를 나타내는 유일한 data class
data class MainState(
    val counterState: CounterState = CounterState(),
    val logState: LogState = LogState(),
    val timeState: TimeState = TimeState(),
    val wifiState: WifiState = WifiState(),
) : UiState {
    companion object {
        fun initial(): MainState = MainState(
            counterState = CounterState(count = 0),
            logState = LogState(),
            timeState = TimeState(),
        )
    }
}

data class CounterState(
    val count: Int = 0,
)

data class LogState(
    val logs: List<Pair<String, String>> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class TimeState(
    val timeString: String = "시간 정보 없음",
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class WifiState(
    val isWifiAvailable: Boolean = false,
)

sealed class MainEffect : UiEffect