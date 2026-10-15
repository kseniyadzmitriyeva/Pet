package com.furiqd.auth.domain.usecase

import com.furiqd.shared.domain.model.User
import com.furiqd.auth.data.repository.AuthRepository
import com.furiqd.auth.data.repository.TokenRepository
import com.furiqd.shared.domain.repository.UserRepository
import javax.inject.Inject

class SignInWithEmailAndPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Boolean> {
        return try {
            val token = authRepository.signInWithEmailAndPassword(email, password).getOrThrow()
            tokenRepository.saveToken(token)
            userRepository.syncSession()

            val isOnboardingCompleted = userRepository.checkIsOnboardingCompleted()
            Result.success(isOnboardingCompleted)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class SignUpWithEmailAndPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Boolean> {
        return try {
            val token = authRepository.signUpWithEmailAndPassword(email, password).getOrThrow()
            tokenRepository.saveToken(token)
            userRepository.saveUser(
                User(
                    id = "",
                    name = "",
                    avatarId = null,
                    isOnboardingCompleted = false,
                    roomIds = emptyList(),
                    email = email,
                    nickName = null
                )
            )
            Result.success(false)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(idToken: String): Result<Boolean> = try {
        val authResult = authRepository.signInWithGoogle(idToken).getOrThrow()
        tokenRepository.saveToken(authResult.token)
        userRepository.syncSession()
        val existingUser = userRepository.getUser()
        
        val isOnboardingCompleted = if (existingUser == null) {
            val newUser = User(
                id = "",
                name = authResult.name.orEmpty(),
                avatarId = null,
                isOnboardingCompleted = false,
                roomIds = emptyList(),
                email = authResult.email,
                nickName = null
            )
            userRepository.saveUser(newUser)
            false
        } else {
            existingUser.isOnboardingCompleted
        }
        
        Result.success(isOnboardingCompleted)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

data class AuthUseCases @Inject constructor(
    val signInWithEmailAndPassword: SignInWithEmailAndPasswordUseCase,
    val signUpWithEmailAndPassword: SignUpWithEmailAndPasswordUseCase,
    val signInWithGoogle: SignInWithGoogleUseCase
)