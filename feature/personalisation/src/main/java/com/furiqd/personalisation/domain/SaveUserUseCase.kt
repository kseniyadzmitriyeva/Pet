package com.furiqd.personalisation.domain

import com.furiqd.shared.model.User
import com.furiqd.shared.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        if (user.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Имя пользователя не может быть пустым"))
        }

        return userRepository.updateUser(user)
    }
}