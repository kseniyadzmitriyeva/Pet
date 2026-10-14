package com.furiqd.shared.usecase

import com.furiqd.shared.repository.StorageRepository
import com.furiqd.shared.repository.UserRepository
import javax.inject.Inject

class UpdateAvatarUseCase@Inject constructor(
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray): Result<String> {
        runCatching {
            val user = userRepository.getUser()
                ?: return Result.failure(Exception("User not found"))
            val downloadUrl = storageRepository.uploadImage(imageBytes,
                "avatars/${user.id}",
                "ITS_ALIVE")
                .getOrElse { return Result.failure(it) }
            userRepository.updateUser(user.copy(avatarId = downloadUrl))
            return Result.success(downloadUrl)
        }
        return Result.failure(Exception("Something went wrong"))
    }
}