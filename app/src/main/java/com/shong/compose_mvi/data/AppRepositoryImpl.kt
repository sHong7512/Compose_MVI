package com.shong.compose_mvi.data

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.shong.compose_mvi.data.local.db.model.AppLog
import com.shong.compose_mvi.data.local.db.model.LogDao
import com.shong.compose_mvi.data.local.memory.MemoryStore
import com.shong.compose_mvi.data.local.preference.EncryptedPref
import com.shong.compose_mvi.data.remote.time.TimeAPIInterface
import com.shong.compose_mvi.data.remote.time.model.TimeResponse
import com.shong.compose_mvi.data.remote.tomo.DEFAULT_FAIL_MASSAGE
import com.shong.compose_mvi.data.remote.tomo.NO_RESULT_MASSAGE
import com.shong.compose_mvi.data.remote.tomo.TomoAPIInterface
import com.shong.compose_mvi.data.remote.tomo.TomoNetworkConfig
import com.shong.compose_mvi.data.remote.tomo.TomoResponse
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.Scrap
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.ScrapData
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.UserInfo
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.Version
import com.shong.compose_mvi.domain.repository.AppRepository
import com.shong.compose_mvi.util.AssetLoader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID
import javax.inject.Inject

private const val USE_DUMMY = true

class AppRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logDao: LogDao,
    private val tomoAPIInterface: TomoAPIInterface,
    private val timeAPIInterface: TimeAPIInterface,
    private val enPref: EncryptedPref,
    private val ms: MemoryStore,
) : AppRepository {
    override suspend fun getInternetTime(): Result<TimeResponse> {
        try {
            val body =
                if (USE_DUMMY) AssetLoader.getJson<TimeResponse>(context)
                else timeAPIInterface.getCurrentTime()
                    .body() ?: throw Exception(DEFAULT_FAIL_MASSAGE)

            return Result.success(body)
        } catch (e: Throwable) {
            e.stackTrace
            return Result.failure(Exception(e.localizedMessage ?: DEFAULT_FAIL_MASSAGE, e))
        }
    }

    override suspend fun getOrCreateUniqueID(): String = withContext(Dispatchers.IO) {
        var idBuf = enPref.getUniqueID()

        if (idBuf.isEmpty()) {
            idBuf = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            if (idBuf.isNullOrEmpty()) {
                idBuf = UUID.randomUUID().toString()
            }
            enPref.setUniqueID(idBuf)
        }

        idBuf
    }

    private suspend inline fun <reified T> safeApiCall(
        cacheGetter: () -> T?,
        cacheSetter: (T?) -> Unit,
        crossinline apiCall: suspend () -> Response<TomoResponse<T>>,
        onSuccess: (T) -> Unit = {},
    ): Result<T> {
        try {
            val cachedData = cacheGetter()
            if (cachedData != null) return Result.success(cachedData)

            val result: T
            if (USE_DUMMY) {
                result = AssetLoader.getJson<T>(context)
            } else {
                val response: Response<TomoResponse<T>> = withContext(Dispatchers.IO) {
                    apiCall()
                }
                val body = response.body()
                val status = TomoNetworkConfig.getStatus(body?.status)
                if (!status.isOk || body == null) {
                    throw Exception(body?.fault ?: DEFAULT_FAIL_MASSAGE)
                }

                if (T::class == Unit::class) {
                    val result = Unit as T
                    cacheSetter(result)
                    onSuccess(result)
                    return Result.success(result)
                }

                result = body.result ?: throw Exception(NO_RESULT_MASSAGE)
            }

            cacheSetter(result)
            onSuccess(result)
            return Result.success(result)
        } catch (e: Throwable) {
            e.stackTrace
            cacheSetter(null)
            return Result.failure(Exception(e.localizedMessage ?: DEFAULT_FAIL_MASSAGE, e))
        }
    }

    override suspend fun deleteScrap(scrapId: Int): Result<Unit> {
        return safeApiCall(
            cacheGetter = { null },
            cacheSetter = { },
            apiCall = {
                tomoAPIInterface.deleteScrap(token = enPref.getToken(), scrapId = scrapId)
            }
        )
    }

    override suspend fun postScrap(activityId: Int): Result<ScrapData> {
        return safeApiCall(
            cacheGetter = { null },
            cacheSetter = { },
            apiCall = {
                tomoAPIInterface.postScrap(
                    token = enPref.getToken(),
                    charset = "UTF-8",
                    chunked = false,
                    activityId = activityId,
                    contentType = "application/x-www-form-urlencoded",
                )
            }
        )
    }

    override suspend fun getScrap(): Result<Scrap> {
        return safeApiCall(
            cacheGetter = { ms.scrap },
            cacheSetter = { ms.scrap = it },
            apiCall = {
                tomoAPIInterface.getScrap(token = enPref.getToken())
            }
        )
    }

    override suspend fun getVersion(): Result<Version> {
        val result = safeApiCall(
            cacheGetter = { ms.version },
            cacheSetter = { ms.version = it },
            apiCall = {
                val device = getOrCreateUniqueID()
                tomoAPIInterface.getVersion(
                    deviceModel = Build.DEVICE, token = enPref.getToken(),
                    version = "4.0.0", deviceId = device,
                    firmware = Build.DISPLAY,
                )
            },
            onSuccess = { version ->
                TomoNetworkConfig.resourceDomain = version.resourceDomain
            }
        )
        // getVersion 실패 시 특별한 처리
        if (result.isFailure) {
            enPref.setToken("")
            ms.clear()
        }
        return result
    }

    override suspend fun postLogin(id: String, pw: String): Result<UserInfo> {
        try {
            if (ms.userInfo != null) return Result.success(ms.userInfo!!)

            val result: UserInfo
            val token: String
            if (USE_DUMMY) {
                result = AssetLoader.getJson<UserInfo>(context)
                token = "token_dummy"
            } else {
                val response: Response<TomoResponse<UserInfo>>
                withContext(Dispatchers.IO) {
                    val device = getOrCreateUniqueID()
                    response = tomoAPIInterface.postLogin(
                        passwd = getMD5(pw), id = id, device = device,
                    )
                }
                val status = TomoNetworkConfig.getStatus(response.body()?.status)
                val body = response.body()
                if (!status.isOk || body == null) throw Exception(response.body()?.fault)
                if (body.result == null) throw Exception(NO_RESULT_MASSAGE)
                result = body.result
                token = response.headers()["x-auth-token"]
                    ?: throw Exception("$DEFAULT_FAIL_MASSAGE\nToken is NULL")
            }
            enPref.setToken(token)

            ms.userInfo = result
            ms.loginToken = token
            return Result.success(result)
        } catch (e: Throwable) {
            ms.clear()
            return Result.failure(Exception(e.localizedMessage ?: DEFAULT_FAIL_MASSAGE, e))
        }
    }

    private fun getMD5(str: String): String {
        return try {
            val md = MessageDigest.getInstance("MD5")
            md.update(str.toByteArray())
            val byteData = md.digest()
            val sb = StringBuffer()
            for (i in byteData.indices) {
                sb.append(((byteData[i].toInt() and 0xff) + 0x100).toString(16).substring(1))
            }
            sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
    }

    override suspend fun addLog(msg: String, sec: Long) {
        delay(300)
        logDao.insertAllDB(AppLog(msg, sec))
    }

    override suspend fun getAllLogs(): List<AppLog> {
        delay(300)
        return logDao.getAllDB()
    }

    override suspend fun removeLogs() {
        delay(300)
        logDao.nukeTableDB()
    }
}