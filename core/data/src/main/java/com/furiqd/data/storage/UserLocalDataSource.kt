package com.furiqd.data.storage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface UserLocalDataSource {
    val isOnboardingCompletedFlow: Flow<Boolean>
    suspend fun getOnboardingStatus(): Boolean
    suspend fun saveOnboardingStatus(isCompleted: Boolean)
    suspend fun clearLocalData()
}

class UserLocalDataSourceImpl@Inject constructor(
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

    override suspend fun clearLocalData() {
        preferencesManager.clear()
    }
}