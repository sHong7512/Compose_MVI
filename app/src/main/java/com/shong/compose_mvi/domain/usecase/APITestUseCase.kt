package com.shong.compose_mvi.domain.usecase

import com.shong.compose_mvi.domain.repository.AppRepository
import com.shong.compose_mvi.util.logI
import javax.inject.Inject

class APITestUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke(): Result<Unit> {
        val apiCalls = mapOf(
            "internetTime" to repository.getInternetTime(),
            "login" to repository.postLogin(id = "unzxc031", pw = "1234"),
            "version" to repository.getVersion(),
            "scrap" to repository.getScrap(),
            // TODO:: 아래 2개는 더미데이터 안넣어둠 (테스트 불가)
//            "(post)scrap" to repository.postScrap(2823),
//            "(delete)scrap" to repository.deleteScrap(428157),
        )
        val failures = mutableMapOf<String, Throwable>()
        for ((apiName, result) in apiCalls) {
            if (result.isFailure) {
                val originalException = result.exceptionOrNull()
                val errorMessage = "API call '$apiName' failed."
                failures[apiName] = originalException ?: Throwable(errorMessage);
            }
        }
        if (failures.isNotEmpty()) {
            val combinedErrorMessage = buildString {
                append("Multiple API tests failed (${failures.size} total):\n")
                failures.forEach { (apiName, exception) ->
                    append("  - API '$apiName' failed with: ${exception.message}\n")
                }
            }

            val primaryCause = failures.values.first()
            return Result.failure(Throwable(combinedErrorMessage, primaryCause))
        }

        return Result.success(Unit)
    }
}