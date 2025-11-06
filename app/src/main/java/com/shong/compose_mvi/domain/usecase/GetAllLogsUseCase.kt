package com.shong.compose_mvi.domain.usecase

import com.shong.compose_mvi.data.local.db.model.AppLog
import com.shong.compose_mvi.domain.repository.AppRepository
import javax.inject.Inject

class GetAllLogsUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke(): List<AppLog> = repository.getAllLogs()
}