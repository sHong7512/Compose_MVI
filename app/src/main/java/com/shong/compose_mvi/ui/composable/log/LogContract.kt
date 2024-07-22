package com.shong.compose_mvi.ui.composable.log

import com.shong.compose_mvi.data.db.AppLog
import com.shong.compose_mvi.ui.composable.UiEffect
import com.shong.compose_mvi.ui.composable.UiEvent
import com.shong.compose_mvi.ui.composable.UiState

sealed class LogEvent : UiEvent {
    data class AddLog(val msg: String) : LogEvent()
    data object ClearLogs : LogEvent()
}

sealed class LogState : UiState {
    data object Init : LogState()
    data object Loading : LogState()
    data class Fail(val msg: String) : LogState()
    data class Success(val data: List<AppLog>) : LogState()
}

sealed class LogEffect : UiEffect {
    data object ShowToast : LogEffect()
}