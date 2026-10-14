package com.furiqd.data.di

import com.furiqd.data.repositories.AuthRepositoryImpl
import com.furiqd.data.repositories.AvatarRepositoryImpl
import com.furiqd.data.repositories.RoomRepositoryImpl
import com.furiqd.data.repositories.StorageRepositoryImpl
import com.furiqd.data.repositories.TokenRepositoryImpl
import com.furiqd.data.repositories.UserRepositoryImpl
import com.furiqd.shared.repository.AuthRepository
import com.furiqd.shared.repository.AvatarRepository
import com.furiqd.shared.repository.RoomRepository
import com.furiqd.shared.repository.StorageRepository
import com.furiqd.shared.repository.TokenRepository
import com.furiqd.shared.repository.UserRepository
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
    abstract fun bindAvatarRepository(
        avatarRepositoryImpl: AvatarRepositoryImpl
    ): AvatarRepository

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