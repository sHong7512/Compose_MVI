package com.shong.compose_mvi.data.remote

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

// api 로그 제공 싱글톤 객체
@InstallIn(SingletonComponent::class)
@Module
object LoggingInterceptor {

    @Singleton
    @Provides
    fun provideRetrofitLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor {
            Log.d("Retrofit API", it)
        }.apply {
//            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
//            else HttpLoggingInterceptor.Level.NONE
        }
    }

}