package com.furiqd.pairing.domain.usecase

import com.furiqd.pairing.data.repository.PairingRepository
import com.furiqd.shared.domain.repository.UserRepository
import javax.inject.Inject

internal class JoinRoomUseCase@Inject constructor(
    private val userRepository: UserRepository,
    private val roomRepository: PairingRepository,
) {
    suspend operator fun invoke(code: String): Result<String> {
        val user = userRepository.getUser()
            ?: return Result.failure(Exception("User not found"))
        return roomRepository.joinRoom(code,user.id)
    }
}