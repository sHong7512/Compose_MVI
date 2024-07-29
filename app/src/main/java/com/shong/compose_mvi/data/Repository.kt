package com.shong.compose_mvi.data

import com.shong.compose_mvi.data.db.AppLog
import com.shong.compose_mvi.data.db.LogDao
import com.shong.compose_mvi.data.remote.model.TimeResponse
import com.shong.compose_mvi.data.remote.retrofitClient.TimeInterface
import com.shong.compose_mvi.data.remote.retrofitClient.TimeRetrofitApi
import kotlinx.coroutines.delay

class Repository constructor(private val logDao: LogDao, timeApi: TimeRetrofitApi) {
    private val epInterface: TimeInterface = timeApi.createClient()

    suspend fun getInternetTime(): TimeResponse? {
        delay(500)
        return epInterface.getCurrentTime().body()
    }

    suspend fun addLog(msg: String, sec: Long) {
        delay(500)
        logDao.insertAllDB(AppLog(msg, sec))
    }

    suspend fun getAllLogs(): List<AppLog> {
        delay(500)
        return logDao.getAllDB()
    }

    suspend fun removeLogs() {
        delay(500)
        logDao.nukeTableDB()
    }
}