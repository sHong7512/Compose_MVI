package com.shong.compose_mvi.presentation.screen.main

import androidx.lifecycle.viewModelScope
import com.shong.compose_mvi.data.local.db.model.AppLog
import com.shong.compose_mvi.domain.usecase.AddLogUseCase
import com.shong.compose_mvi.domain.usecase.GetAllLogsUseCase
import com.shong.compose_mvi.domain.usecase.GetInternetTimeUseCase
import com.shong.compose_mvi.domain.usecase.RemoveLogsUseCase
import com.shong.compose_mvi.presentation.screen.BaseViewModel
import com.shong.compose_mvi.presentation.util.DateFormatter
import com.shong.compose_mvi.util.logD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// 메인 뷰모델
@HiltViewModel
class MainViewModel @Inject constructor(
    private val dateFormatter: DateFormatter,
    val addLogUseCase: AddLogUseCase,
    val removeLogsUseCase: RemoveLogsUseCase,
    val getAllLogsUseCase: GetAllLogsUseCase,
    val getInternetTimeUseCase: GetInternetTimeUseCase,
) :
    BaseViewModel<MainEvent, MainState, MainEffect>() {
    override fun createInitialState(): MainState = MainState.initial()

    init {
        initialLogs()
    }

    override fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.Increment -> {
                // 현재 상태의 count + 1로 counterState를 복사하고
                // 이 counterState로 MainState를 다시 복사
                updateState {
                    it.copy(
                        counterState = it.counterState.copy(
                            count = it.counterState.count + 1
                        )
                    )
                }
            }

            is MainEvent.Decrement -> {
                updateState {
                    it.copy(
                        counterState = it.counterState.copy(
                            count = it.counterState.count - 1
                        )
                    )
                }
            }

            is MainEvent.AddLog -> {
                viewModelScope.launch {
                    try {
                        addLogUseCase.invoke(event.msg, System.currentTimeMillis() / 1000)
                        val result = getAllLogsUseCase.invoke()
                        updateState {
                            it.copy(
                                logState = it.logState.copy(
                                    logs = convert(result),
                                    isLoading = false,
                                    error = null
                                )
                            )
                        }
                    } catch (e: Throwable) {
                        updateState {
                            it.copy(
                                logState = it.logState.copy(
                                    isLoading = false,
                                    error = "데이터 가져오기 실패 ${e.localizedMessage}"
                                )
                            )
                        }
                    }
                }
            }

            MainEvent.RemoveLogs -> {
                viewModelScope.launch {
                    try {
                        removeLogsUseCase.invoke()
                        val result = getAllLogsUseCase.invoke()
                        updateState {
                            it.copy(
                                logState = it.logState.copy(
                                    logs = convert(result), // 빈 리스트가 됨
                                    isLoading = false,
                                    error = null
                                )
                            )
                        }
                    } catch (e: Throwable) {
                        updateState {
                            it.copy(
                                logState = it.logState.copy(
                                    isLoading = false,
                                    error = "데이터 가져오기 실패 ${e.localizedMessage}"
                                )
                            )
                        }
                    }
                }
            }

            MainEvent.GetDeviceTime -> {
                val deviceTimeStr = dateFormatter.format(System.currentTimeMillis() / 1000)
                updateState {
                    it.copy(
                        timeState = it.timeState.copy(
                            timeString = deviceTimeStr,
                            isLoading = false,
                            error = null
                        )
                    )
                }
            }

            MainEvent.GetInternetTime -> {
                // 시간 요청 시작: 로딩 상태
                updateState {
                    it.copy(
                        timeState = it.timeState.copy(
                            isLoading = true,
                            error = null
                        )
                    )
                }

                viewModelScope.launch {
                    try {
                        val result = getInternetTimeUseCase.invoke()
                        val unixTimestamp = result.getOrNull()?.unixTime
                        if (unixTimestamp == null) {
                            throw NullPointerException("unixTimestamp is null")
                        } else {
                            logD("$unixTimestamp // ${System.currentTimeMillis() / 1000}")
                            // 성공: 시간 문자열 업데이트
                            updateState {
                                it.copy(
                                    timeState = it.timeState.copy(
                                        timeString = dateFormatter.format(unixTimestamp),
                                        isLoading = false,
                                        error = null
                                    )
                                )
                            }
                        }
                    } catch (e: Throwable) {
                        // 실패: 에러 메시지 업데이트
                        updateState {
                            it.copy(
                                timeState = it.timeState.copy(
                                    isLoading = false,
                                    error = "시간 요청 에러 :: $e"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initialLogs() {
        if (currentState.logState.isLoading) return

        updateState {
            it.copy(
                logState = it.logState.copy(
                    isLoading = true,
                    error = null
                )
            )
        }

        viewModelScope.launch {
            try {
                val result = getAllLogsUseCase.invoke()
                updateState {
                    it.copy(
                        logState = it.logState.copy(
                            logs = convert(result),
                            isLoading = false,
                            error = null
                        )
                    )
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        logState = it.logState.copy(
                            isLoading = false,
                            error = "데이터 가져오기 실패 ${e.localizedMessage}"
                        )
                    )
                }
            }
        }
    }

    private fun convert(appLogs: List<AppLog>): List<Pair<String, String>> {
        val buf = mutableListOf<Pair<String, String>>()

        for (a in appLogs) {
            buf.add(Pair(a.msg, dateFormatter.format(a.micros)))
        }

        return buf
    }

}