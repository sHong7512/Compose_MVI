package com.shong.compose_mvi.ui.composable.time

import androidx.lifecycle.viewModelScope
import com.shong.compose_mvi.data.Repository
import com.shong.compose_mvi.ui.composable.BaseViewModel
import com.shong.compose_mvi.util.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class TimeViewModel @Inject constructor(
    private val repository: Repository,
    private val dateFormatter: DateFormatter,
) :
    BaseViewModel<TimeEvent, TimeState, TimeEffect>() {
    override fun createInitialState(): TimeState {
        return TimeState.Initial
    }

    override fun handleEvent(event: TimeEvent) {
        if (currentState is TimeState.Loading) return
        setState(TimeState.Loading)
        viewModelScope.launch {
            asyncHandleEvent(event)
        }
    }

    private suspend fun asyncHandleEvent(event: TimeEvent) {
        val newState: TimeState = when (event) {
            is TimeEvent.GetInternetTime -> {
                try {
                    val unixTimestamp = repository.getInternetTime()?.unixTime
                    if (unixTimestamp == null) {
                        throw NullPointerException("unixTimestamp is null")
                    } else {
                        val instant = Instant.ofEpochSecond(unixTimestamp)
                        val zoneId = ZoneId.of("Asia/Seoul")
                        val localDateTime = LocalDateTime.ofInstant(instant, zoneId)
                        TimeState.Success(dateStr = dateFormatter.format(localDateTime))
                    }
                } catch (e: Exception) {
                    TimeState.Fail("시간 요청 에러 :: $e")
                }
            }

            is TimeEvent.GetDeviceTime -> {
                TimeState.Success(dateStr = dateFormatter.format(LocalDateTime.now()))
            }
        }
        setState(newState)
    }

    fun showEffect() {
        setEffect(TimeEffect.ShowToast)
    }
}