package com.shong.compose_mvi.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AppLog::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao

    companion object {
        const val DATABASE_NAME = "logging.db"
    }
}
