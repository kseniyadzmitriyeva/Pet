package com.furiqd.local.datasource

import com.furiqd.local.contract.UserLocalDataSource
import com.furiqd.local.entity.LocalUserEntity
import com.furiqd.local.storage.UserPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val preferencesManager: UserPreferencesManager
) : UserLocalDataSource {

    override val isOnboardingCompletedFlow: Flow<Boolean> =
        preferencesManager.isOnboardingCompleted

    override suspend fun getOnboardingStatus(): Boolean {
        return preferencesManager.isOnboardingCompleted.first()
    }

    override suspend fun saveOnboardingStatus(isCompleted: Boolean) {
        preferencesManager.saveOnboardingStatus(isCompleted)
    }

    override fun observeUser(): Flow<LocalUserEntity?> {
        return preferencesManager.userData.map { prefs ->
            if (prefs.id.isEmpty()) null else LocalUserEntity(
                id = prefs.id,
                name = prefs.name,
                avatarId = prefs.avatarId
            )
        }
    }

    override suspend fun getUser(): LocalUserEntity? {
        val prefs = preferencesManager.userData.first()
        return if (prefs.id.isEmpty()) null else LocalUserEntity(
            id = prefs.id,
            name = prefs.name,
            avatarId = prefs.avatarId
        )
    }

    override suspend fun saveUser(user: LocalUserEntity) {
        preferencesManager.saveUser(
            id = user.id,
            name = user.name,
            avatarId = user.avatarId
        )
    }

    // --- Очистка ---

    override suspend fun clearLocalData() {
        preferencesManager.clear()
    }
}