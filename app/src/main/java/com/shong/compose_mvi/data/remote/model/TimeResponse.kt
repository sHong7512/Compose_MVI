package com.shong.compose_mvi.data.remote.model

import com.google.gson.annotations.SerializedName

data class TimeResponse(
    @SerializedName("unixtime")
    val unixTime: Long,
)