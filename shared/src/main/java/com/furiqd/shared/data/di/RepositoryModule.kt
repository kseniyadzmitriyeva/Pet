package com.furiqd.shared.data.di

import com.furiqd.shared.data.repositories.AuthRepositoryImpl
import com.furiqd.shared.data.repositories.RoomRepositoryImpl
import com.furiqd.shared.data.repositories.StorageRepositoryImpl
import com.furiqd.shared.data.repositories.TokenRepositoryImpl
import com.furiqd.shared.data.repositories.UserRepositoryImpl
import com.furiqd.shared.domain.repository.AuthRepository
import com.furiqd.shared.domain.repository.RoomRepository
import com.furiqd.shared.domain.repository.StorageRepository
import com.furiqd.shared.domain.repository.TokenRepository
import com.furiqd.shared.domain.repository.UserRepository
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
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindRoomRepository(
        roomRepositoryImpl: RoomRepositoryImpl
    ): RoomRepository

    @Binds
    @Singleton
    abstract fun bindStorageRepository(
        storageRepositoryImpl: StorageRepositoryImpl
    ): StorageRepository
}