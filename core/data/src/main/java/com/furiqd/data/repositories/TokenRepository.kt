package com.furiqd.data.repositories

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TokenRepository {
    val accessToken: Flow<String>
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}

class TokenRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<String>
) : TokenRepository {

    override val accessToken: Flow<String> = dataStore.data

    override suspend fun saveToken(token: String) {
        dataStore.updateData { token }
    }

    override suspend fun clearToken() {
        dataStore.updateData { "" }
    }
}