package com.furiqd.home.domain.repository

import com.furiqd.shared.domain.model.PairingRoom
import kotlinx.coroutines.flow.Flow

internal interface HomeRepository {
    fun observeUserRooms(userId: String): Flow<List<PairingRoom>>
    suspend fun deleteRoom(roomId: String)
}