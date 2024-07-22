package com.shong.compose_mvi.ui.composable.log

import androidx.lifecycle.viewModelScope
import com.shong.compose_mvi.data.Repository
import com.shong.compose_mvi.logE
import com.shong.compose_mvi.ui.composable.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(val repository: Repository) :
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
                    repository.addLog(event.msg)
                }

                is LogEvent.ClearLogs -> {
                    repository.removeLogs()
                }
            }
            LogState.Success(repository.getAllLogs())
        } catch (e: Exception) {
            LogState.Fail("데이터 가져오기 실패 ${e.localizedMessage}")
        }
        setState(newState)
    }

    fun initialLogs() {
        if (currentState is LogState.Loading) return
        setState(LogState.Loading)
        viewModelScope.launch {
            val newState: LogState = try {
                LogState.Success(repository.getAllLogs())
            } catch (e: Exception) {
                logE("$e")
                LogState.Fail("데이터 가져오기 실패 ${e.localizedMessage}")
            }
            setState(newState)
        }
    }
}