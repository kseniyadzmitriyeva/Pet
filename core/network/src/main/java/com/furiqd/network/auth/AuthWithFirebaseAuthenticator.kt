package com.furiqd.network.auth

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

    suspend fun signInWithGoogleToken(idToken: String): Result<String?> {
        return try {
            val authCredential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(authCredential).await()
            val firebaseSessionToken = result.user?.getIdToken(false)?.await()?.token
            Result.success(firebaseSessionToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}