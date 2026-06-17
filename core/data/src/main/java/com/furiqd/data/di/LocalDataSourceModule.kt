package com.furiqd.data.di

import com.furiqd.data.storage.UserLocalDataSource
import com.furiqd.local.datasource.UserLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindUserLocalDataSource(
        userLocalDataSourceImpl: UserLocalDataSourceImpl
    ): UserLocalDataSource
}