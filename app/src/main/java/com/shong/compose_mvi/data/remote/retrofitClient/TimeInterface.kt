package com.shong.compose_mvi.data.remote.retrofitClient

import com.shong.compose_mvi.data.remote.model.TimeResponse
import retrofit2.Response
import retrofit2.http.GET

interface TimeInterface {
    @GET("timezone/Etc/UTC")
    suspend fun getCurrentTime(): Response<TimeResponse>
}