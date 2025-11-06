package com.shong.compose_mvi.data.remote.tomo.model.tomonote_info

import com.google.gson.annotations.SerializedName

// 버전 및 기타정보 모델
data class Version(
    @SerializedName("userInfo")
    val userInfo: UserInfo,
    @SerializedName("resourceDomain")
    val resourceDomain: String?,
    @SerializedName("contentDomain")
    val contentDomain: String?,
    @SerializedName("initFile")
    val initFile: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("updateUrl")
    val updateUrl: String?,
    @SerializedName("lastVer")
    val lastVer: String?,
    @SerializedName("force")
    val force: String?,
    @SerializedName("needUpdate")
    val needUpdate: Boolean,
    @SerializedName("keywords")
    val keywords: List<Keyword>?,
    @SerializedName("recommends")
    val recommends: List<Recommend>?,
)

data class Keyword(
    @SerializedName("keyword")
    val keyword: String?,
    @SerializedName("main_yn")
    val main_yn: String?,
)
data class Recommend(
    @SerializedName("id")
    val id: Int,
    @SerializedName("activityId")
    val activityId: Int,
    @SerializedName("image")
    val image: String?,
    @SerializedName("description")
    val description: String?,
)
