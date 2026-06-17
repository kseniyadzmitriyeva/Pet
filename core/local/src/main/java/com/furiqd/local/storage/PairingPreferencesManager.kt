package com.furiqd.local.storage

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "pairing_prefs")

@Singleton
class PairingPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val ROOM_ID_KEY = stringPreferencesKey("room_id")
    private val IS_PAIRING_COMPLETE_KEY = booleanPreferencesKey("is_pairing_complete")

    val roomId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[ROOM_ID_KEY] }

    val isPairingComplete: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_PAIRING_COMPLETE_KEY] ?: false }

    suspend fun saveRoomId(roomId: String) {
        context.dataStore.edit { preferences ->
            preferences[ROOM_ID_KEY] = roomId
        }
    }

    suspend fun setPairingComplete(isComplete: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_PAIRING_COMPLETE_KEY] = isComplete
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.remove(ROOM_ID_KEY)
            preferences.remove(IS_PAIRING_COMPLETE_KEY)
        }
    }
}
