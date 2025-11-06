package com.shong.compose_mvi.data.di

import com.shong.compose_mvi.data.AppRepositoryImpl
import com.shong.compose_mvi.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppRepository(repository: AppRepositoryImpl): AppRepository
}