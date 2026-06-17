package com.furiqd.local.datasource

import androidx.datastore.core.DataStore
import com.furiqd.local.contract.TokenLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenLocalDataSourceImpl @Inject constructor(
    private val tokenDataStore: DataStore<String>
) : TokenLocalDataSource {

    override fun observeToken(): Flow<String?> {
        return tokenDataStore.data.map { token ->
            token.ifEmpty { null }
        }
    }

    override suspend fun getToken(): String? {
        val token = tokenDataStore.data.first()
        return token.ifEmpty { null }
    }

    override suspend fun saveToken(token: String) {
        tokenDataStore.updateData {
            token
        }
    }

    override suspend fun clearToken() {
        tokenDataStore.updateData { "" }
    }
}