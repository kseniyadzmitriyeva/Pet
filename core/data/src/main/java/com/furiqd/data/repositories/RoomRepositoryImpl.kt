package com.furiqd.data.repositories

import com.furiqd.data.mapper.toDomain
import com.furiqd.data.mapper.toEntity
import com.furiqd.domain.model.PairingRoom
import com.furiqd.domain.repository.RoomRepository
import com.furiqd.local.datasource.PairingLocalDataSource
import com.furiqd.network.contract.PairingRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val remoteDataSource: PairingRemoteDataSource,
    private val localDataSource: PairingLocalDataSource
) : RoomRepository {

    override suspend fun createRoom(hostUserId: String): Result<String> {
        return runCatching {
            var uniqueCode: String
            do {
                uniqueCode = (100000..999999).random().toString()
            } while (remoteDataSource.isCodeExisting(uniqueCode))
            val roomId = remoteDataSource.createRoom(hostUserId, uniqueCode)
            localDataSource.saveRoomId(roomId)

            roomId
        }
    }

    override suspend fun joinRoom(
        inviteCode: String,
        friendUserId: String
    ): Result<Unit> {
        return runCatching {
            val roomId = remoteDataSource.executeJoinTransaction(inviteCode, friendUserId)
            localDataSource.saveRoomId(roomId)
            localDataSource.setPairingComplete(true)
        }
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
}