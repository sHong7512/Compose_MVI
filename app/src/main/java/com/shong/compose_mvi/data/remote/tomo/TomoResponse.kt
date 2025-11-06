package com.shong.compose_mvi.data.remote.tomo

import com.google.gson.annotations.SerializedName

data class TomoResponse<T>(
    @SerializedName("status")
    val status: Int,
    @SerializedName("fault")
    val fault: String?,
    @SerializedName("version")
    val version: Int?,
    @SerializedName("result")
    val result: T?,
)