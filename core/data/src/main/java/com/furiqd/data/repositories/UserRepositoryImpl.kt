package com.furiqd.data.repositories

import com.furiqd.data.di.ApplicationScope
import com.furiqd.data.mapper.toDto
import com.furiqd.domain.model.User
import com.furiqd.domain.model.UserSessionState
import com.furiqd.domain.repository.UserRepository
import com.furiqd.local.datasource.UserLocalDataSource
import com.furiqd.local.entity.LocalUserEntity
import com.furiqd.network.contract.RemoteAuthDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
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
        remoteDataSource.currentUserIdFlow
            .flatMapLatest { userId ->
                if (userId == null) {
                    localDataSource.clearLocalData()
                    flowOf(UserSessionState.NotAuthorized)
                } else {
                    localDataSource.isOnboardingCompletedFlow.onEach { isCompleted ->
                        if (isCompleted) {
                            _sessionState.value = UserSessionState.FullyAuthorized
                        } else {
                            _sessionState.value = UserSessionState.NeedsOnboarding
                        }
                    }
                }
            }
            .onEach { state ->
                if (state is UserSessionState.NotAuthorized) {
                    _sessionState.value = state
                }
            }
            .launchIn(scope)
    }

    override suspend fun syncSession(): Result<Unit> {
        return runCatching {
            val userId = remoteDataSource.currentUserId ?: throw Exception("User not logged in")
            val remoteUserDto = remoteDataSource.getUserProfile(userId)

            if (remoteUserDto != null) {
                localDataSource.saveUser(
                    LocalUserEntity(
                        id = remoteUserDto.id,
                        name = remoteUserDto.name,
                        avatarId = remoteUserDto.avatarId
                    )
                )
                localDataSource.saveOnboardingStatus(remoteUserDto.isOnboardingCompleted)
            }
        }
    }

    override suspend fun completeOnboarding(user: User): Result<Unit> {
        return runCatching {
            val userId = remoteDataSource.currentUserId ?: throw Exception("No active user")
            val completedUser = user.copy(id = userId, isOnboardingCompleted = true)
            remoteDataSource.saveUserProfile(completedUser.toDto())
            localDataSource.saveUser(
                LocalUserEntity(id = userId, name = user.name, avatarId = user.avatarId)
            )
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