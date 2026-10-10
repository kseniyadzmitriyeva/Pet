package com.furiqd.home.domain.usecase

import com.furiqd.domain.repository.AuthRepository
import com.furiqd.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase@Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.logOut()
    }
}