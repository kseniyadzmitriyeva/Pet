package com.furiqd.auth.data.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


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