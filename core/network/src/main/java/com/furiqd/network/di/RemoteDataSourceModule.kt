package com.furiqd.network.di

import com.furiqd.network.BuildConfig
import com.furiqd.network.contract.RemoteAuthDataSource
import com.furiqd.network.contract.RemoteImageDataSource
import com.furiqd.network.contract.RemotePairingDataSource
import com.furiqd.network.datasource.FirebaseAuthDataSource
import com.furiqd.network.datasource.FirebasePairingDataSource
import com.furiqd.network.datasource.GoogleStorageDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceModule {
    @Provides
    @Singleton
    fun provideRemoteAuthDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): RemoteAuthDataSource {
        return FirebaseAuthDataSource(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideRemotePairingDataSource(
        firestore: FirebaseFirestore
    ): RemotePairingDataSource {
        return FirebasePairingDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRemoteImageDataSource(
        @Named("workerEndpoint") workerEndpoint: String,
        okHttpClient: OkHttpClient
    ): RemoteImageDataSource {
        return GoogleStorageDataSource(workerEndpoint, okHttpClient)
    }
}