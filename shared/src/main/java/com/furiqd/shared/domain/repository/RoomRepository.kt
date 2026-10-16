package com.furiqd.shared.domain.repository

import com.furiqd.shared.domain.model.PairingRoom
import com.furiqd.shared.domain.model.PetEggType
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun observeCurrentRoom(roomId: String): Flow<PairingRoom?>
    suspend fun nudgeFriend(roomId: String, senderUserId: String): Result<Unit>
    suspend fun fetchRoomDetails(roomId: String): Result<PairingRoom>
}