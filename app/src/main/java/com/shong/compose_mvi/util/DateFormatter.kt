package com.shong.compose_mvi.util

import android.annotation.SuppressLint
import dagger.hilt.android.scopes.ViewModelScoped
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@ViewModelScoped
class DateFormatter @Inject constructor() {

    private val formatter = DateTimeFormatter.ofPattern("dd일 MM월 yyyy년 HH시 mm분 ss초")

    fun format(localDateTime: LocalDateTime): String {
        return formatter.format(localDateTime)
    }
}
