package com.furiqd.network.contract

import com.furiqd.network.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface RemoteAuthDataSource {
    val currentUserIdFlow: Flow<String?>
    val currentUserId: String?
    suspend fun getUserProfile(userId: String): UserDto?
    suspend fun saveUserProfile(userDto: UserDto)
    suspend fun signOut()
}