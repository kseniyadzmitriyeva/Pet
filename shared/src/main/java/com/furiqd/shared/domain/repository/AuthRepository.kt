package com.furiqd.shared.domain.repository

import com.furiqd.shared.domain.model.AuthResult

interface AuthRepository{
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String>
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<String>
    suspend fun signInWithGoogle(idToken: String): Result<AuthResult>
}