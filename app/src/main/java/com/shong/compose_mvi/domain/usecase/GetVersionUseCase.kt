package com.shong.compose_mvi.domain.usecase

import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.Version
import com.shong.compose_mvi.domain.repository.AppRepository
import javax.inject.Inject

class GetVersionUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke(): Result<Version> = repository.getVersion()
}