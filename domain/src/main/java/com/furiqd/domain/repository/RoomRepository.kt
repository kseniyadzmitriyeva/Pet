package com.furiqd.domain.repository

import com.furiqd.domain.model.PairingRoom
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun createRoom(hostUserId: String): Result<String>
    suspend fun joinRoom(inviteCode: String, friendUserId: String): Result<Unit>
    fun observeCurrentRoom(roomId: String): Flow<PairingRoom?>
    suspend fun checkUserHasActiveRoom(userId: String): Result<String?>
    suspend fun uploadUserPhoto(roomId: String, userId: String, imageBytes: ByteArray): Result<Unit>
    suspend fun nudgeFriend(roomId: String, senderUserId: String): Result<Unit>
    suspend fun fetchRoomDetails(roomId: String): Result<PairingRoom>
    fun observeUserRooms(userId: String): Flow<List<PairingRoom>>
}