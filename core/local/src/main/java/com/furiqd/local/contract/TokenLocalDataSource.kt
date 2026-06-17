package com.furiqd.local.contract

import kotlinx.coroutines.flow.Flow

interface TokenLocalDataSource {
    fun observeToken(): Flow<String?>
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}