package com.furiqd.auth.domain.usecase

import com.furiqd.data.repositories.AuthRepository
import com.furiqd.data.repositories.TokenRepository
import javax.inject.Inject

class SignInWithEmailAndPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return try {
            val token = authRepository.signInWithEmailAndPassword(email, password).getOrThrow()
            tokenRepository.saveToken(token)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class SignUpWithEmailAndPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return try {
            val token = authRepository.signUpWithEmailAndPassword(email, password).getOrThrow()
            tokenRepository.saveToken(token)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(idToken: String): Result<Unit> {
        return try {
            val token = repository.signInWithGoogle(idToken).getOrThrow()
            tokenRepository.saveToken(token)
            Result.success(Unit)
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