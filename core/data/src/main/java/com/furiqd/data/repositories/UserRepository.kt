package com.furiqd.data.repositories

import com.furiqd.data.di.ApplicationScope
import com.furiqd.data.models.UserSessionState
import com.furiqd.data.storage.UserLocalDataSource
import com.furiqd.network.auth.RemoteAuthDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface UserRepository {
    val sessionState: StateFlow<UserSessionState>
    suspend fun syncSession(): Result<Unit>
    suspend fun completeOnboarding(): Result<Unit>
    suspend fun logOut(): Result<Unit>
    suspend fun checkIsOnboardingCompleted(): Boolean
}

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteAuthDataSource,
    private val localDataSource: UserLocalDataSource,
    @ApplicationScope private val scope: CoroutineScope
) : UserRepository {

    private val _sessionState = MutableStateFlow<UserSessionState>(UserSessionState.Loading)
    override val sessionState: StateFlow<UserSessionState> = _sessionState.asStateFlow()

    init {
        observeAuthChanges()
    }

    private fun observeAuthChanges() {
        scope.launch {
            remoteDataSource.currentUserIdFlow.collect { userId ->
                if (userId == null) {
                    _sessionState.value = UserSessionState.NotAuthorized
                    localDataSource.clearLocalData()
                } else {
                    localDataSource.isOnboardingCompletedFlow.collect { isCompleted ->
                        if (isCompleted) {
                            _sessionState.value = UserSessionState.FullyAuthorized
                        } else {
                            _sessionState.value = UserSessionState.NeedsOnboarding
                        }
                    }
                }
            }
        }
    }

    override suspend fun syncSession(): Result<Unit> {
        return runCatching {
            val userId = remoteDataSource.currentUserId ?: throw Exception("User not logged in")
            val isServerOnboardingCompleted = remoteDataSource.checkOnboardingStatus(userId)

            if (isServerOnboardingCompleted) {
                localDataSource.saveOnboardingStatus(true)
            }
        }
    }

    override suspend fun completeOnboarding(): Result<Unit> {
        return runCatching {
            val userId = remoteDataSource.currentUserId ?: throw Exception("No active user")

            remoteDataSource.saveOnboardingStatus(userId, isCompleted = true)
            localDataSource.saveOnboardingStatus(true)
        }
    }

    override suspend fun logOut(): Result<Unit> {
        return runCatching {
            remoteDataSource.signOut()
            localDataSource.clearLocalData()
        }
    }

    override suspend fun checkIsOnboardingCompleted(): Boolean {
        return localDataSource.getOnboardingStatus()
    }
}
