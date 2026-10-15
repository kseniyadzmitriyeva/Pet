package com.furiqd.shared.data.repositories

import com.furiqd.shared.data.di.ApplicationScope
import com.furiqd.shared.data.mapper.toDomain
import com.furiqd.shared.data.mapper.toDto
import com.furiqd.shared.data.mapper.toLocal
import com.furiqd.shared.domain.model.User
import com.furiqd.shared.domain.model.UserSessionState
import com.furiqd.shared.domain.repository.UserRepository
import com.furiqd.local.contract.UserLocalDataSource
import com.furiqd.network.contract.RemoteAuthDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteAuthDataSource,
    private val localDataSource: UserLocalDataSource,
    @ApplicationScope private val scope: CoroutineScope
) : UserRepository {

    override val sessionState: StateFlow<UserSessionState> = remoteDataSource.currentUserIdFlow
        .flatMapLatest { userId ->
            if (userId == null) {
                localDataSource.clearLocalData()
                flowOf(UserSessionState.NotAuthorized)
            } else {
                localDataSource.observeUser().map { localEntity ->
                    val user = localEntity?.toDomain()
                    when {
                        user == null -> UserSessionState.Loading
                        user.isOnboardingCompleted -> UserSessionState.FullyAuthorized
                        else -> UserSessionState.NeedsOnboarding
                    }
                }
            }
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = UserSessionState.Loading
        )

    override fun observeUser(): Flow<User?> {
        return localDataSource.observeUser().map { it?.toDomain() }
    }

    override suspend fun getUser(): User? {
        return localDataSource.getUser()?.toDomain()
    }

    override suspend fun syncSession(): Result<Unit> {
        return runCatching {
            val userId = remoteDataSource.currentUserId ?: throw Exception("User not logged in")
            val remoteUserDto = remoteDataSource.getUserProfile(userId)

            if (remoteUserDto != null) {
                val domainUser = remoteUserDto.toDomain()
                localDataSource.saveUser(domainUser.toLocal())
                localDataSource.saveOnboardingStatus(domainUser.isOnboardingCompleted)
            }
        }
    }

    override suspend fun saveUser(user: User): Result<Unit> {
        return runCatching {
            val userId = remoteDataSource.currentUserId ?: throw Exception("No active user")
            val completedUser = user.copy(id = userId)
            remoteDataSource.saveUserProfile(completedUser.toDto())
            localDataSource.saveUser(completedUser.toLocal())
            localDataSource.saveOnboardingStatus(user.isOnboardingCompleted)
        }
    }

    override suspend fun logOut(): Result<Unit> {
        return runCatching {
            remoteDataSource.signOut()
            localDataSource.clearLocalData()
        }
    }

    override suspend fun checkIsOnboardingCompleted(): Boolean {
        return localDataSource.getUser()?.isOnboardingCompleted ?: false
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        return runCatching {
            val userId = remoteDataSource.currentUserId ?: throw Exception("No active user")
            val localUser = localDataSource.getUser()
            val currentEmail = localUser?.email ?: ""
            val completedUser = user.copy(
                id = userId,
                email = currentEmail
            )

            remoteDataSource.saveUserProfile(completedUser.toDto())
            localDataSource.saveUser(completedUser.toLocal())
            localDataSource.saveOnboardingStatus(user.isOnboardingCompleted)
        }
    }

    override suspend fun getUserById(userId: String): User? {
        return remoteDataSource.getUserProfile(userId)?.toDomain()
    }
}
