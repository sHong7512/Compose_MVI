package com.shong.compose_mvi.data.remote.tomo.model.tomonote_info

import com.google.gson.annotations.SerializedName

// 로그인 모델
data class UserInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("instituteName")
    val instituteName: String?,
    @SerializedName("className")
    val className: String?,
    @SerializedName("age")
    val age: Int?,
    @SerializedName("serviceMode")
    val serviceMode: String?,
    @SerializedName("teacherAge")
    val teacherAge: Int?,
    @SerializedName("tomobooks")
    val tomobooks: String?,
    @SerializedName("tomonote")
    val tomonote: String?,
    @SerializedName("tomonoteRole")
    val tomonoteRole: String?,
    @SerializedName("branch")
    val branch: String?,
)