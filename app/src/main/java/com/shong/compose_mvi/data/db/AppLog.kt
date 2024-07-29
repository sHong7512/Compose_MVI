package com.shong.compose_mvi.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logs")
data class AppLog(val msg: String, val micros: Long) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
