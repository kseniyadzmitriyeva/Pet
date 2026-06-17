package com.furiqd.local.datasource

import com.furiqd.local.entity.JointPetEntity
import kotlinx.coroutines.flow.Flow

interface PairingLocalDataSource {
    fun getRoomId(): Flow<String?>
    fun isPairingComplete(): Flow<Boolean>
    suspend fun saveRoomId(roomId: String)
    suspend fun setPairingComplete(isComplete: Boolean)
    fun observeCachedPet(): Flow<JointPetEntity?>
    suspend fun cachePet(pet: JointPetEntity)
    suspend fun clearLocalSession()
}