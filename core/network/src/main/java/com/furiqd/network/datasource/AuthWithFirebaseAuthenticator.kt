package com.furiqd.network.datasource

import com.furiqd.network.dto.AuthResultDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthWithFirebaseAuthenticator @Inject constructor(
    val auth: FirebaseAuth
) {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseSessionToken = result.user?.getIdToken(false)?.await()?.token
            Result.success(firebaseSessionToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<String?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseSessionToken = result.user?.getIdToken(false)?.await()?.token
            Result.success(firebaseSessionToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogleToken(idToken: String): Result<AuthResultDto> {
        return try {
            val authCredential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(authCredential).await()

            val firebaseUser = result.user ?: throw Exception("Firebase user is null")
            val firebaseSessionToken = firebaseUser.getIdToken(false).await().token
                ?: throw Exception("Failed to get Firebase session token")

            val authResult = AuthResultDto(
                token = firebaseSessionToken,
                email = firebaseUser.email ?: throw Exception("Google account missing email"),
                name = firebaseUser.displayName
            )

            Result.success(authResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}