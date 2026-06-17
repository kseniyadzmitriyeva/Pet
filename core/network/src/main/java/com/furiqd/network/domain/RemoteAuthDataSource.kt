package com.furiqd.network.domain

import kotlinx.coroutines.flow.Flow

interface RemoteAuthDataSource {
    val currentUserIdFlow: Flow<String?>
    val currentUserId: String?
    suspend fun checkOnboardingStatus(userId: String): Boolean
    suspend fun saveOnboardingStatus(userId: String, isCompleted: Boolean): Boolean
    suspend fun signOut()
}