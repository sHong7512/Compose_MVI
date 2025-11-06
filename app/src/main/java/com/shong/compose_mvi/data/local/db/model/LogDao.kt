package com.shong.compose_mvi.data.local.db.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LogDao {
    @Query("SELECT * FROM logs ORDER BY id DESC")
    suspend fun getAllDB(): List<AppLog>

    @Insert
    suspend fun insertAllDB(vararg logs: AppLog)

    @Query("DELETE FROM logs")
    suspend fun nukeTableDB()
}
