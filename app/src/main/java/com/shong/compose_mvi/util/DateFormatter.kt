package com.shong.compose_mvi.util

import dagger.hilt.android.scopes.ViewModelScoped
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ViewModelScoped
class DateFormatter @Inject constructor() {

    private val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")

    fun format(timeStamp: Long): String {
        val instant = Instant.ofEpochSecond(timeStamp)
        val zoneId = ZoneId.of("Asia/Seoul")
        val localDateTime = LocalDateTime.ofInstant(instant, zoneId)
        return formatter.format(localDateTime)
    }
}
