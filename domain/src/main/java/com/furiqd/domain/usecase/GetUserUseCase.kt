package com.furiqd.domain.usecase

import com.furiqd.domain.model.User
import com.furiqd.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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