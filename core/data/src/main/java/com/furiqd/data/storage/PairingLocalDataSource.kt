package com.furiqd.data.storage

import com.furiqd.data.models.JointPet
import kotlinx.coroutines.flow.Flow

interface PairingLocalDataSource {
    fun getRoomId(): Flow<String?>
    fun isPairingComplete(): Flow<Boolean>
    suspend fun saveRoomId(roomId: String)
    suspend fun setPairingComplete(isComplete: Boolean)
    fun observeCachedPet(): Flow<JointPet?>
    suspend fun cachePet(pet: JointPet)
    suspend fun clearLocalSession()
}