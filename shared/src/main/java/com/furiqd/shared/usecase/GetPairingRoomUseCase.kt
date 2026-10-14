package com.furiqd.shared.usecase

import com.furiqd.shared.model.PairingRoom
import com.furiqd.shared.repository.RoomRepository
import javax.inject.Inject

class GetPairingRoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(roomId: String): Result<PairingRoom> {
        return roomRepository.fetchRoomDetails(roomId)
    }
}