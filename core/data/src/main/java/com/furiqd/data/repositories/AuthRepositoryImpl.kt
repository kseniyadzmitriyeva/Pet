package com.furiqd.data.repositories

import com.furiqd.domain.repository.AuthRepository
import com.furiqd.network.datasource.AuthWithFirebaseAuthenticator
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
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

    override suspend fun signInWithGoogle(idToken: String): Result<String> {
        val result = authWithFirebaseAuthenticator.signInWithGoogleToken(idToken)
        return mapTokenResult(result)
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