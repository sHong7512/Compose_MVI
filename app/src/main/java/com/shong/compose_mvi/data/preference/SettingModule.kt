package com.shong.compose_mvi.data.preference

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SettingModule {
    @Singleton
    @Provides
    fun provideCalSettingPref(@ApplicationContext context: Context): SettingPref {
        return SettingPref(context)
    }

}