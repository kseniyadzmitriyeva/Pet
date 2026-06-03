package com.furiqd.auth.domain.usecase

import com.furiqd.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
){
    suspend operator fun invoke(idToken: String): Result<Unit> {
        return repository.signInWithGoogle(idToken)
    }
}