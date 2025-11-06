package com.shong.compose_mvi.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shong.compose_mvi.data.local.db.model.AppLog
import com.shong.compose_mvi.data.local.db.model.LogDao

@Database(entities = [AppLog::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao

    companion object {
        const val DATABASE_NAME = "logging.db"
    }
}
