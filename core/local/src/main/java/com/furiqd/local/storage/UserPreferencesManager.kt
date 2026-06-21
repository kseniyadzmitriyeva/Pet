package com.furiqd.local.storage

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")

    companion object {
        private val ONBOARDING_KEY = booleanPreferencesKey("is_onboarding_completed")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_AVATAR_ID_KEY = intPreferencesKey("user_avatar_id")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[ONBOARDING_KEY] ?: false }

    suspend fun saveOnboardingStatus(isCompleted: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_KEY] = isCompleted
        }
    }

    val userData: Flow<UserPreferencesData> = context.dataStore.data
        .map { preferences ->
            UserPreferencesData(
                id = preferences[USER_ID_KEY] ?: "",
                name = preferences[USER_NAME_KEY] ?: "",
                isOnboardingCompleted = preferences[ONBOARDING_KEY] ?: false,
                avatarId = preferences[USER_AVATAR_ID_KEY]
            )
        }
    suspend fun saveUser(id: String, name: String, avatarId: Int?) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = id
            preferences[USER_NAME_KEY] = name
            if (avatarId != null) {
                preferences[USER_AVATAR_ID_KEY] = avatarId
            } else {
                preferences.remove(USER_AVATAR_ID_KEY)
            }
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}