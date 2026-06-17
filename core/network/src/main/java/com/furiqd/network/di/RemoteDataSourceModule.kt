package com.furiqd.network.di

import com.furiqd.network.contract.RemoteAuthDataSource
import com.furiqd.network.contract.RemotePairingDataSource
import com.furiqd.network.datasource.FirebaseAuthDataSource
import com.furiqd.network.datasource.FirebasePairingDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}