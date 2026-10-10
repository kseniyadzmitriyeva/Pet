package com.furiqd.domain.usecase

import com.furiqd.domain.repository.StorageRepository
import com.furiqd.domain.repository.UserRepository
import javax.inject.Inject

class UpdateAvatarUseCase@Inject constructor(
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray): Result<Unit> {
        val user = userRepository.getUser()
            ?: return Result.failure(Exception("User not found"))
        val downloadUrl = storageRepository.uploadImage(imageBytes,
            "avatars/${user.id}",
            "ITS_ALIVE")
            .getOrElse { return Result.failure(it) }
        return userRepository.updateUser(user.copy(avatarId = downloadUrl))
    }
}