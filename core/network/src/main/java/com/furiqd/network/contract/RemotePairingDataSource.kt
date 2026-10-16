package com.furiqd.network.contract

import com.furiqd.network.dto.JointPetDto
import com.furiqd.network.dto.PairingRoomDto
import kotlinx.coroutines.flow.Flow

interface RemotePairingDataSource {
    fun observeRoom(roomId: String): Flow<PairingRoomDto?>
    suspend fun sendNudge(roomId: String, senderUserId: String)
    suspend fun getRoomDetails(roomId: String): PairingRoomDto?
}