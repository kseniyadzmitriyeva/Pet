package com.furiqd.shared.data.repositories

import com.furiqd.shared.data.mapper.toDomain
import com.furiqd.shared.data.mapper.toEntity
import com.furiqd.shared.domain.model.PairingRoom
import com.furiqd.shared.domain.model.PetEggType
import com.furiqd.shared.domain.repository.RoomRepository
import com.furiqd.local.contract.PairingLocalDataSource
import com.furiqd.network.contract.RemotePairingDataSource
import com.furiqd.network.dto.JointPetDto
import com.furiqd.network.dto.PairingRoomDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.map

class RoomRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemotePairingDataSource,
    private val localDataSource: PairingLocalDataSource
) : RoomRepository {

    override suspend fun createRoom(hostUserId: String,
                                    petName: String,
                                    petEggType: PetEggType): Result<String> {
        return runCatching {
            var uniqueCode: String
            do {
                uniqueCode = (100000..999999).random().toString()
            } while (remoteDataSource.isCodeExisting(uniqueCode))
            val roomDto = PairingRoomDto(
                code = uniqueCode,
                hostId = hostUserId,
                friendId = null,
                status = "WAITING",
                createdAt = Date(),
                pet = JointPetDto(
                    id = UUID.randomUUID().toString(),
                    name = petName,
                    petEggType = petEggType.id,
                    stage = "EGG",
                    level = 1,
                    experience = 0,
                    hatchedAt = null
                )
            )
            val roomId = remoteDataSource.createRoom(roomDto)
            localDataSource.saveRoomId(roomId)
            roomId
        }
    }

    override suspend fun joinRoom(
        inviteCode: String,
        friendUserId: String
    ): Result<String> {
        runCatching {
            val roomId = remoteDataSource.executeJoinTransaction(inviteCode, friendUserId)
            localDataSource.saveRoomId(roomId)
            localDataSource.setPairingComplete(true)
            return Result.success(roomId)
        }
        return Result.failure(Exception("Failed to join room"))
    }

    override fun observeCurrentRoom(roomId: String): Flow<PairingRoom?> {
        return remoteDataSource.observeRoom(roomId)
            .map { remoteRoomDto ->
                if (remoteRoomDto == null) return@map null

                val domainRoom = remoteRoomDto.toDomain()
                val localPetEntity = domainRoom.pet.toEntity()
                localDataSource.cachePet(localPetEntity)

                domainRoom
            }
    }

    override suspend fun checkUserHasActiveRoom(userId: String): Result<String?> {
        return runCatching {
            val roomId = remoteDataSource.checkActiveRoom(userId)
            if (roomId != null) {
                localDataSource.saveRoomId(roomId)
                localDataSource.setPairingComplete(true)
            }
            roomId
        }
    }

    override suspend fun uploadUserPhoto(
        roomId: String,
        userId: String,
        imageBytes: ByteArray
    ): Result<Unit> {
        return runCatching {
            remoteDataSource.uploadUserPhoto(roomId, userId, imageBytes)
        }
    }

    override suspend fun nudgeFriend(
        roomId: String,
        senderUserId: String
    ): Result<Unit> {
        return runCatching {
            remoteDataSource.sendNudge(roomId, senderUserId)
        }
    }

    override suspend fun fetchRoomDetails(roomId: String): Result<PairingRoom> {
        return runCatching {
            val remoteRoomDto = remoteDataSource.getRoomDetails(roomId)
                ?: throw NoSuchElementException("Room with id $roomId not found")

            val domainRoom = remoteRoomDto.toDomain()
            val localPetEntity = domainRoom.pet.toEntity()
            localDataSource.cachePet(localPetEntity)

            domainRoom
        }
    }

    override fun observeUserRooms(userId: String): Flow<List<PairingRoom>> {
        return remoteDataSource.observeUserRooms(userId)
            .map { dtoList ->
                dtoList.map { it.toDomain() }
            }
    }
}