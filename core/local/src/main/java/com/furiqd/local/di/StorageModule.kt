package com.furiqd.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.furiqd.local.contract.TokenLocalDataSource
import com.furiqd.local.datasource.TokenLocalDataSourceImpl
import com.furiqd.local.storage.CryptoManager
import com.furiqd.local.storage.TokenSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideSecureDataStore(
        @ApplicationContext context: Context,
        cryptoManager: CryptoManager
    ): DataStore<String> {
        return DataStoreFactory.create(
            serializer = TokenSerializer(cryptoManager),
            produceFile = { context.dataStoreFile("secure_token.pb") }
        )
    }

    @Provides
    @Singleton
    fun provideTokenLocalDataSource(
        dataStore: DataStore<String>
    ): TokenLocalDataSource {
        return TokenLocalDataSourceImpl(dataStore)
    }
}