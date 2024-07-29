package com.shong.compose_mvi.ui.composable.log

import androidx.lifecycle.viewModelScope
import com.shong.compose_mvi.data.Repository
import com.shong.compose_mvi.logE
import com.shong.compose_mvi.ui.composable.BaseViewModel
import com.shong.compose_mvi.util.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val repository: Repository,
    private val dateFormatter: DateFormatter,
) :
    BaseViewModel<LogEvent, LogState, LogEffect>() {
    override fun createInitialState(): LogState {
        return LogState.Init
    }

    init {
        initialLogs()
    }

    override fun handleEvent(event: LogEvent) {
        if (currentState is LogState.Loading) return
        setState(LogState.Loading)
        viewModelScope.launch {
            asyncHandleEvent(event)
        }
    }

    private suspend fun asyncHandleEvent(event: LogEvent) {
        val newState: LogState = try {
            when (event) {
                is LogEvent.AddLog -> {
                    repository.addLog(event.msg, System.currentTimeMillis() / 1000)
                }

                is LogEvent.ClearLogs -> {
                    repository.removeLogs()
                }
            }
            LogState.Success(KorTimeLog.convert(repository.getAllLogs(), dateFormatter))
        } catch (e: Exception) {
            LogState.Fail("데이터 가져오기 실패 ${e.localizedMessage}")
        }
        setState(newState)
    }

    private fun initialLogs() {
        if (currentState is LogState.Loading) return
        setState(LogState.Loading)
        viewModelScope.launch {
            val newState: LogState = try {
                LogState.Success(KorTimeLog.convert(repository.getAllLogs(), dateFormatter))
            } catch (e: Exception) {
                logE("$e")
                LogState.Fail("데이터 가져오기 실패 ${e.localizedMessage}")
            }
            setState(newState)
        }
    }
}