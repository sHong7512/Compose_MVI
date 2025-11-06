package com.shong.compose_mvi.data.remote.tomo.model.tomonote_info

import com.google.gson.annotations.SerializedName

data class Scrap(
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("datas")
    val datas: List<ScrapData>,
)

data class ScrapData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("regDate")
    val regDate: Long,
    @SerializedName("scrapId")
    val scrapId: Int,
)