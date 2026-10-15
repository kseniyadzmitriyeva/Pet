package com.furiqd.auth.data.repository

import com.furiqd.network.datasource.AuthWithFirebaseAuthenticator
import com.furiqd.shared.domain.model.AuthResult
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authWithFirebaseAuthenticator: AuthWithFirebaseAuthenticator
) : AuthRepository {

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<String> {
        val result = authWithFirebaseAuthenticator.signInWithEmailAndPassword(email, password)
        return mapTokenResult(result)
    }

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Result<String> {
        val result = authWithFirebaseAuthenticator.signUpWithEmailAndPassword(email, password)
        return mapTokenResult(result)
    }

    override suspend fun signInWithGoogle(idToken: String): Result<AuthResult> {
        return runCatching {
            val firebaseResult = authWithFirebaseAuthenticator.signInWithGoogleToken(idToken)
            val firebaseUser = firebaseResult.getOrThrow()
            AuthResult(
                token = idToken,
                email = firebaseUser.email,
                name = firebaseUser.name
            )
        }
    }
    private fun mapTokenResult(networkResult: Result<String?>): Result<String> {
        return networkResult.fold(
            onSuccess = { token ->
                if (token != null) {
                    Result.success(token)
                } else {
                    Result.failure(Exception("Can't get token"))
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}