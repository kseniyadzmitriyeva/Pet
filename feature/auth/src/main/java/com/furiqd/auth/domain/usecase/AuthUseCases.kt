package com.furiqd.auth.domain.usecase

import com.furiqd.data.repositories.AuthRepository
import com.furiqd.data.repositories.TokenRepository
import com.furiqd.data.repositories.UserRepository
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
            val isOnboardingCompleted = userRepository.checkIsOnboardingCompleted()
            Result.success(isOnboardingCompleted)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class SignUpWithEmailAndPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Boolean> {
        return try {
            val token = authRepository.signUpWithEmailAndPassword(email, password).getOrThrow()
            tokenRepository.saveToken(token)
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
    suspend operator fun invoke(idToken: String): Result<Boolean> {
        return try {
            val token = authRepository.signInWithGoogle(idToken).getOrThrow()
            tokenRepository.saveToken(token)
            val isOnboardingCompleted = userRepository.checkIsOnboardingCompleted()
            Result.success(isOnboardingCompleted)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class AuthUseCases @Inject constructor(
    val signInWithEmailAndPassword: SignInWithEmailAndPasswordUseCase,
    val signUpWithEmailAndPassword: SignUpWithEmailAndPasswordUseCase,
    val signInWithGoogle: SignInWithGoogleUseCase
)