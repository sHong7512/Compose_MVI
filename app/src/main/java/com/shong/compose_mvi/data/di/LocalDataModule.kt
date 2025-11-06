package com.shong.compose_mvi.data.di

import android.content.Context
import androidx.room.Room
import com.shong.compose_mvi.data.local.db.AppDatabase
import com.shong.compose_mvi.data.local.db.model.LogDao
import com.shong.compose_mvi.data.local.memory.MemoryStore
import com.shong.compose_mvi.data.local.preference.EncryptedPref
import com.shong.compose_mvi.data.local.preference.SettingPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Singleton
    @Provides
    fun provideSettingPref(@ApplicationContext context: Context): SettingPref =
        SettingPref(context)

    @Singleton
    @Provides
    fun provideEncryptedPref(@ApplicationContext context: Context): EncryptedPref =
        EncryptedPref(context)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext, AppDatabase::class.java, AppDatabase.Companion.DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideLogDao(database: AppDatabase): LogDao = database.logDao()

    @Singleton
    @Provides
    fun provideMemoryStore(): MemoryStore = MemoryStore()
}