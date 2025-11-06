package com.shong.compose_mvi.data.remote.tomo

import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.Scrap
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.ScrapData
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.UserInfo
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.Version
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// 토모노트 api 인터페이스
interface TomoAPIInterface {
    // 로그인
    @POST("tomonote_info/login")
    @FormUrlEncoded
    suspend fun postLogin(
        @Field("passwd") passwd: String,
        @Field("id") id: String,
        @Field("device") device: String? = null, // DEVICE_ID
    ): Response<TomoResponse<UserInfo>>

    // 버전 체크, 초기 세팅 정보
    @GET("tomonote_info/version/{version}/{deviceId}")
    suspend fun getVersion(
        @Header("X-DEVICE-MODEL") deviceModel: String,
        @Header("X-AUTH-TOKEN") token: String,
        @Path("version") version: String,
        @Path("deviceId") deviceId: String,
        @Query("firmware") firmware: String,
    ): Response<TomoResponse<Version>>

    // 내 수업 자료실
    @GET("tomonote_info/scrap")
    suspend fun getScrap(
        @Header("X-AUTH-TOKEN") token: String,
    ): Response<TomoResponse<Scrap>>

    // my버튼으로 저장 (내 수업 자료실 저장)
    @POST("tomonote_info/scrap")
    @FormUrlEncoded
    suspend fun postScrap(
        @Header("X-AUTH-TOKEN") token: String,
        @Header("charset") charset: String,
        @Header("Chunked") chunked: Boolean,
        @Header("Content-Type") contentType: String,
        @Field("activityId") activityId: Int,
    ): Response<TomoResponse<ScrapData>>

    // 내 수업 자료실 컨텐츠 삭제
    @DELETE("tomonote_info/scrap/{scrapId}")
    suspend fun deleteScrap(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("scrapId") scrapId: Int
    ): Response<TomoResponse<Unit>>
}