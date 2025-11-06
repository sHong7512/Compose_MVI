package com.shong.compose_mvi.domain.usecase

import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.UserInfo
import com.shong.compose_mvi.domain.repository.AppRepository
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke(id: String, pw: String): Result<UserInfo> =
        repository.postLogin(id, pw)
}