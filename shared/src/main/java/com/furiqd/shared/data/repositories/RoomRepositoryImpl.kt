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
}