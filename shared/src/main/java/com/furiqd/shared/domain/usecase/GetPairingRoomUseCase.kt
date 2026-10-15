package com.furiqd.shared.domain.usecase

import com.furiqd.shared.domain.model.PairingRoom
import com.furiqd.shared.domain.repository.RoomRepository
import javax.inject.Inject

class GetPairingRoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(roomId: String): Result<PairingRoom> {
        return roomRepository.fetchRoomDetails(roomId)
    }
}