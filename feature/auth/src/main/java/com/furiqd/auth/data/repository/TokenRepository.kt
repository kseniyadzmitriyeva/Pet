package com.furiqd.auth.data.repository

import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    val accessToken: Flow<String>
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}