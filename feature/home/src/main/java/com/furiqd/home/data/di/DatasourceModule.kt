package com.furiqd.home.data.di

import com.furiqd.home.data.remote.HomeRemoteDataSource
import com.furiqd.home.data.remote.HomeRemoteDataSourceImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal  class DatasourceModule {
    @Provides
    @Singleton
    fun provideRemoteHomeDataSource(
        firestore: FirebaseFirestore
    ): HomeRemoteDataSource {
        return HomeRemoteDataSourceImpl(firestore)
    }
}