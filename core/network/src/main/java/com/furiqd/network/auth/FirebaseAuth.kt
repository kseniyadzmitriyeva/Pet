package com.furiqd.network.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthWithFirebaseAuthenticator @Inject constructor(
    val auth: FirebaseAuth
) {
    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
    }

    fun signUpWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
    }

    suspend fun signInWithGoogleToken(idToken: String): Result<Unit> {
        return try {
            val authCredential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(authCredential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}