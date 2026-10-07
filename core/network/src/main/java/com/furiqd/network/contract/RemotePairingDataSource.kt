package com.furiqd.network.contract

import com.furiqd.network.dto.PairingRoomDto
import kotlinx.coroutines.flow.Flow

interface RemotePairingDataSource {
    suspend fun isCodeExisting(code: String): Boolean
    suspend fun createRoom(hostUserId: String, uniqueCode: String): String
    suspend fun executeJoinTransaction(inviteCode: String, friendUserId: String): String
    fun observeRoom(roomId: String): Flow<PairingRoomDto?>
    suspend fun checkActiveRoom(userId: String): String?
    suspend fun uploadUserPhoto(roomId: String, userId: String, imageBytes: ByteArray)
    suspend fun sendNudge(roomId: String, senderUserId: String)
    suspend fun getRoomDetails(roomId: String): PairingRoomDto?
    fun observeUserRooms(userId: String): Flow<List<PairingRoomDto>>
}