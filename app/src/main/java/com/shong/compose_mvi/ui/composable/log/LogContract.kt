package com.shong.compose_mvi.ui.composable.log

import com.shong.compose_mvi.data.db.AppLog
import com.shong.compose_mvi.ui.composable.UiEffect
import com.shong.compose_mvi.ui.composable.UiEvent
import com.shong.compose_mvi.ui.composable.UiState
import com.shong.compose_mvi.util.DateFormatter

sealed class LogEvent : UiEvent {
    data class AddLog(val msg: String) : LogEvent()
    data object ClearLogs : LogEvent()
}

sealed class LogState : UiState {
    data object Init : LogState()
    data object Loading : LogState()
    data class Fail(val msg: String) : LogState()
    data class Success(val data: List<KorTimeLog>) : LogState()
}

data class KorTimeLog(val msg: String, val korTime: String) {
    companion object {
        fun convert(appLogs: List<AppLog>, dateFormatter: DateFormatter): List<KorTimeLog> {
            val buf = mutableListOf<KorTimeLog>()

            for (a in appLogs) {
                buf.add(KorTimeLog(msg = a.msg, korTime = dateFormatter.format(a.micros)))
            }

            return buf
        }
    }
}

sealed class LogEffect : UiEffect {
    data object ShowToast : LogEffect()
}