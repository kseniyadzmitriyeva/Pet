package com.furiqd.domain.repository

interface AuthRepository{
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String>
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<String>
    suspend fun signInWithGoogle(idToken: String): Result<String>
    suspend fun signOut(): Result<Unit>
}