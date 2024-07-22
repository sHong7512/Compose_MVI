package com.shong.compose_mvi.ui.composable.time

import com.shong.compose_mvi.ui.composable.UiEffect
import com.shong.compose_mvi.ui.composable.UiEvent
import com.shong.compose_mvi.ui.composable.UiState

sealed class TimeEvent : UiEvent {
    data object GetInternetTime : TimeEvent()
    data object GetDeviceTime : TimeEvent()
}

sealed class TimeState : UiState {
    data object Initial : TimeState()

    data object Loading : TimeState()

    data class Fail(val error: String) : TimeState()

    data class Success(val dateStr: String) : TimeState()
}

sealed class TimeEffect : UiEffect {
    data object ShowToast : TimeEffect()
}