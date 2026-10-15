package com.furiqd.pairing.data.di

import com.furiqd.pairing.data.repository.PairingRepository
import com.furiqd.pairing.data.repository.PairingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
internal abstract class PairingRepositoryModule {
    @Binds
    abstract fun bindPairingRepository(
        pairingRepositoryImpl: PairingRepositoryImpl
    ): PairingRepository
}