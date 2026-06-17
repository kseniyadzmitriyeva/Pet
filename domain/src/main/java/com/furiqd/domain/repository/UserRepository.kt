package com.furiqd.domain.repository

import com.furiqd.domain.model.User
import com.furiqd.domain.model.UserSessionState
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val sessionState: StateFlow<UserSessionState>
    suspend fun syncSession(): Result<Unit>
    suspend fun completeOnboarding(user: User): Result<Unit>
    suspend fun logOut(): Result<Unit>
    suspend fun checkIsOnboardingCompleted(): Boolean
}