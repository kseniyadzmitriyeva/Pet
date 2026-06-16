package com.furiqd.network.di

import com.furiqd.network.auth.FirebaseAuthDataSource
import com.furiqd.network.auth.RemoteAuthDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideRemoteAuthDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): RemoteAuthDataSource {
        return FirebaseAuthDataSource(auth, firestore)
    }
}