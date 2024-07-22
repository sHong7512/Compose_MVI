package com.shong.compose_mvi.data.preference

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@EntryPoint
@InstallIn(SingletonComponent::class)
interface PrefEntryPoint{
    fun getSettingPref() : SettingPref
}