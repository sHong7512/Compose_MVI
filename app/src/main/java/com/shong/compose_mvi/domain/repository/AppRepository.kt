package com.shong.compose_mvi.domain.repository

import com.shong.compose_mvi.data.local.db.model.AppLog
import com.shong.compose_mvi.data.remote.time.model.TimeResponse
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.Scrap
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.ScrapData
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.UserInfo
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.Version

/**
 * 데이터 통신 규칙을 정의하는 인터페이스.
 * 지금은 API 모델을 직접 사용하지만, 나중에 Domain 모델로 대체할 수 있음
 */
interface AppRepository {
    // ---Network---
    suspend fun postLogin(id: String, pw: String): Result<UserInfo>
    suspend fun getVersion(): Result<Version>
    suspend fun getScrap(): Result<Scrap>
    suspend fun postScrap(activityId: Int): Result<ScrapData>
    suspend fun deleteScrap(scrapId: Int): Result<Unit>

    suspend fun getInternetTime(): Result<TimeResponse>
    // ---Network---

    //  ---Local---
    suspend fun addLog(msg: String, sec: Long)
    suspend fun getAllLogs(): List<AppLog>
    suspend fun removeLogs()
    suspend fun getOrCreateUniqueID(): String
}