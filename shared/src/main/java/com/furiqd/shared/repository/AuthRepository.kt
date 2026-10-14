package com.furiqd.shared.repository

import com.furiqd.shared.model.AuthResult

interface AuthRepository{
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String>
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<String>
    suspend fun signInWithGoogle(idToken: String): Result<AuthResult>
    suspend fun signOut(): Result<Unit>
}