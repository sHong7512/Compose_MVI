package com.shong.compose_mvi.data.remote.tomo

// 네트워크 관련 정의 변수들
const val DEFAULT_FAIL_MASSAGE = "오류가 발생했습니다."
const val NO_RESULT_MASSAGE = "결과가 없습니다."
enum class TomoNetworkConfig(
    val code: Int,
    val isOk: Boolean = false,
    val msg: String = DEFAULT_FAIL_MASSAGE,
) {
    // ok
    HTTP_STATUS_OK(code = 200, isOk = true, msg = "성공"),
    STATUS_OK(code = 0, isOk = true, msg = "성공"),

    // fail
    STATUS_INVALID_AUTH_TOKEN(code = 2000),
    STATUS_USER_NOT_FOUND_ID(code = 2001),
    STATUS_PASSWORD_NOT_MATCH(code = 2002),
    STATUS_NOT_APPROVED_USER(code = 2003),
    STATUS_NOT_APPROVED_DEVICE(code = 2004),
    STATUS_NOT_MATCH_DEVICE(code = 2005),
    STATUS_INTERNAL_ERROR(code = 9000),
    UNDEFINED(code = -1);

    companion object {
        // cdn
        var resourceDomain: String? = null

        fun getStatus(code: Int?): TomoNetworkConfig = entries.find { it.code == code } ?: UNDEFINED
    }
}