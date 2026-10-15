package com.furiqd.pairing.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject

interface PairingLocalDataSource {
    suspend fun saveRoomId(roomId: String)
    suspend fun getRoomId(): String?
    fun observeRoomId(): Flow<String?>
    suspend fun setPairingComplete(isComplete: Boolean)
    suspend fun clearPairingData()
}

internal class PairingLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PairingLocalDataSource {

    private companion object {
        val KEY_ROOM_ID = stringPreferencesKey("pairing_active_room_id")
        val KEY_IS_PAIRING_COMPLETE = booleanPreferencesKey("is_pairing_complete")
    }

    override suspend fun saveRoomId(roomId: String) {
        dataStore.edit { preferences ->
            preferences[KEY_ROOM_ID] = roomId
        }
    }

    override suspend fun getRoomId(): String? {
        return dataStore.data.map { preferences ->
            preferences[KEY_ROOM_ID]
        }.firstOrNull()
    }

    override fun observeRoomId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_ROOM_ID]
        }
    }

    override suspend fun setPairingComplete(isComplete: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_PAIRING_COMPLETE] = isComplete
        }
    }

    override suspend fun clearPairingData() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_ROOM_ID)
            preferences.remove(KEY_IS_PAIRING_COMPLETE)
        }
    }
}