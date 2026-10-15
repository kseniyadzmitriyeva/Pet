package com.furiqd.shared.domain.usecase

import com.furiqd.shared.domain.model.User
import com.furiqd.shared.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase@Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<User?>{
        return try {
            userRepository.observeUser()
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}