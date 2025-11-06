package com.shong.compose_mvi.data.remote.time

import com.shong.compose_mvi.data.remote.time.model.TimeResponse
import retrofit2.Response
import retrofit2.http.GET

interface TimeAPIInterface {
    @GET("timezone/Etc/UTC")
    suspend fun getCurrentTime(): Response<TimeResponse>
}