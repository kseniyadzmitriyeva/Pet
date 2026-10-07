package com.furiqd.home.domain.usecase

import com.furiqd.domain.repository.RoomRepository
import com.furiqd.domain.repository.UserRepository
import javax.inject.Inject

class CreateRoomUseCase@Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
){

    suspend operator fun invoke(petName: String, petEgg: Int): Result<String> {
        val user = userRepository.getUser() ?: return Result.failure(Exception("User not found"))
        val roomId = roomRepository.createRoom(petName)
        userRepository.updateUser(user.copy(
            roomIds = user.roomIds + roomId.getOrNull().toString()
        ))
        return roomId
    }
}