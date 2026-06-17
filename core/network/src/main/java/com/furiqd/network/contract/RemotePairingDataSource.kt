package com.furiqd.network.contract

import com.furiqd.network.dto.PairingRoomDto
import kotlinx.coroutines.flow.Flow

interface PairingRemoteDataSource {
    suspend fun isCodeExisting(code: String): Boolean
    suspend fun createRoom(hostUserId: String, uniqueCode: String): String
    suspend fun executeJoinTransaction(inviteCode: String, friendUserId: String): String
    fun observeRoom(roomId: String): Flow<PairingRoomDto?>
    suspend fun checkActiveRoom(userId: String): String?
}