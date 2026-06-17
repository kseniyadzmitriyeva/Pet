package com.furiqd.data.di

import com.furiqd.data.repositories.AuthRepositoryImpl
import com.furiqd.data.repositories.AvatarRepositoryImpl
import com.furiqd.data.repositories.TokenRepositoryImpl
import com.furiqd.data.repositories.UserRepositoryImpl
import com.furiqd.domain.repository.AuthRepository
import com.furiqd.domain.repository.AvatarRepository
import com.furiqd.domain.repository.TokenRepository
import com.furiqd.domain.repository.UserRepository
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
}