package com.shong.compose_mvi.domain.usecase

import com.shong.compose_mvi.domain.repository.AppRepository
import javax.inject.Inject

class AddLogUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke(msg: String, sec: Long) {
        repository.addLog(msg, sec)
    }
}