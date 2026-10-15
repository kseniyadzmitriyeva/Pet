package com.furiqd.pairing.data.repository

import com.furiqd.pairing.data.datasource.PairingLocalDataSource
import com.furiqd.pairing.data.datasource.PairingRemoteDataSource
import com.furiqd.pairing.data.datasource.RoomException
import com.furiqd.pairing.data.mapper.toDomain
import com.furiqd.shared.model.PairingRoom
import com.furiqd.shared.model.PetEggType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PairingRepository {
    suspend fun createRoom(
        hostUserId: String,
        petName: String,
        petEggType: PetEggType
    ): Result<PairingRoom>

    suspend fun joinRoom(
        inviteCode: String,
        userId: String
    ): Result<String>
    suspend fun getPairingRoom(roomId: String): Result<PairingRoom>
    fun observeRoomState(roomId: String): Flow<PairingRoom>
}

internal class PairingRepositoryImpl @Inject constructor(
    private val remoteDataSource: PairingRemoteDataSource,
    private val localDataSource: PairingLocalDataSource
) : PairingRepository {

    override suspend fun createRoom(hostUserId: String, petName: String, petEggType: PetEggType): Result<PairingRoom> {
        return runCatching {
            val generatedCode = generateUniqueCode()

            val roomId = remoteDataSource.createRoom(
                hostUserId = hostUserId,
                uniqueCode = generatedCode,
                petName = petName,
                petEggTypeId = petEggType.id
            )

            localDataSource.saveRoomId(roomId)

            val roomDto = remoteDataSource.getPairingRoom(roomId)
                ?: throw IllegalStateException("Failed to load room details")

            roomDto.toDomain()
        }
    }

    override suspend fun joinRoom(inviteCode: String, userId: String): Result<String> {
        return runCatching {
            val roomId = remoteDataSource.joinRoom(inviteCode, userId)
            localDataSource.saveRoomId(roomId)
            localDataSource.setPairingComplete(true)

            roomId
        }
    }

    override suspend fun getPairingRoom(roomId: String): Result<PairingRoom> {
        return runCatching {
            val roomDto = remoteDataSource.getPairingRoom(roomId)
                ?: throw RoomException.RoomNotFoundException()
            roomDto.toDomain()
        }
    }

    override fun observeRoomState(roomId: String): Flow<PairingRoom> {
        TODO("Not yet implemented")
    }

    private fun generateUniqueCode(): String {
        return (100000..999999).random().toString()
    }
}