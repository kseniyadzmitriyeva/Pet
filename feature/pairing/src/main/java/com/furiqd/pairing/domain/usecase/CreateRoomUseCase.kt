package com.furiqd.pairing.domain.usecase

import com.furiqd.pairing.data.repository.PairingRepository
import com.furiqd.shared.domain.model.PetEggType
import com.furiqd.shared.domain.repository.RoomRepository
import com.furiqd.shared.domain.repository.UserRepository
import javax.inject.Inject

internal class CreateRoomUseCase@Inject constructor(
    private val roomRepository: PairingRepository,
    private val userRepository: UserRepository
){
    suspend operator fun invoke(petName: String, petEggType: PetEggType): Result<String> {
        return runCatching {
            val user = userRepository.getUser()
                ?: return Result.failure(Exception("User not found"))
            roomRepository.createRoom(user.id, petName, petEggType).getOrThrow()

        }
    }
}