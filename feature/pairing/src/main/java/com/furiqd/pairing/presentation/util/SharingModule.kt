package com.furiqd.pairing.presentation.util

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SharingModule {

    @Binds
    @Singleton
    fun bindClipboardManagerWrapper(
        impl: AndroidClipboardManagerWrapper
    ): ClipboardManagerWrapper

    @Binds
    @Singleton
    fun bindShareManager(
        impl: AndroidShareManager
    ): ShareManager
}