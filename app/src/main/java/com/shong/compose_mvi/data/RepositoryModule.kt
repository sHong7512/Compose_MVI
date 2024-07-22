package com.shong.compose_mvi.data

import android.content.Context
import androidx.room.Room
import com.shong.compose_mvi.data.db.AppDatabase
import com.shong.compose_mvi.data.db.LogDao
import com.shong.compose_mvi.data.remote.retrofitClient.TimeRetrofitApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideLogDao(database: AppDatabase): LogDao {
        return database.logDao()
    }

    @Singleton
    @Provides
    fun provideTimeAPI(httpLoggingInterceptor: HttpLoggingInterceptor): TimeRetrofitApi =
        TimeRetrofitApi(httpLoggingInterceptor)

    @Singleton
    @Provides
    fun provideRepository(logDao: LogDao, timeApi: TimeRetrofitApi): Repository {
        return Repository(logDao, timeApi)
    }

}