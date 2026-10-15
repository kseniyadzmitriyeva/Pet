package com.furiqd.pairing.data.di

import com.furiqd.pairing.data.datasource.FirebasePairingDataSource
import com.furiqd.pairing.data.datasource.PairingLocalDataSource
import com.furiqd.pairing.data.datasource.PairingLocalDataSourceImpl
import com.furiqd.pairing.data.datasource.PairingRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PairingDataSourceModule {
    @Binds
    abstract fun bindPairingRemoteDataSource(
        firebasePairingDataSource: FirebasePairingDataSource
    ): PairingRemoteDataSource

    @Binds
    abstract fun bindPairingLocalDataSource(
        pairingLocalDataSourceImpl: PairingLocalDataSourceImpl
    ): PairingLocalDataSource

}