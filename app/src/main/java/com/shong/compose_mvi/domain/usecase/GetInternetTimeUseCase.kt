package com.shong.compose_mvi.domain.usecase

import com.shong.compose_mvi.domain.repository.AppRepository
import javax.inject.Inject

class GetInternetTimeUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke() = repository.getInternetTime()
}