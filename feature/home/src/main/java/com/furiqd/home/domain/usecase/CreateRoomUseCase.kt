package com.furiqd.home.domain.usecase

import com.furiqd.shared.model.PetEggType
import com.furiqd.shared.repository.RoomRepository
import com.furiqd.shared.repository.UserRepository
import javax.inject.Inject

class CreateRoomUseCase@Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
){

    suspend operator fun invoke(petName: String, petEggType: PetEggType): Result<String> {
        val user = userRepository.getUser()
            ?: return Result.failure(Exception("User not found"))
        val roomId = roomRepository.createRoom(user.id, petName, petEggType)
        userRepository.updateUser(user.copy(
            roomIds = user.roomIds + roomId.getOrNull().toString()
        ))
        return roomId
    }
}