package com.furiqd.network.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthDataSource(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : RemoteAuthDataSource {

    override val currentUserIdFlow: Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.uid)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    override suspend fun checkOnboardingStatus(userId: String): Boolean {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            document.getBoolean("isSettingsConfigured") ?: false
        } catch (e: Exception) {
            // В продакшене здесь логируют ошибку сети
            false
        }
    }

    override suspend fun saveOnboardingStatus(userId: String, isCompleted: Boolean): Boolean {
        return try {
            val data = mapOf("isSettingsConfigured" to isCompleted)
            firestore.collection("users")
                .document(userId)
                .set(data)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signOut() {
        try {
            firebaseAuth.signOut()
        } catch (e: Exception) {
            throw e
        }
    }
}