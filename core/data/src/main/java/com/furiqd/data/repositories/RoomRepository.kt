package com.furiqd.data.repositories

import com.furiqd.data.models.PairingRoom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PairingRepository {
    fun createRoom(hostUserId: String): Flow<Result<String>>
    suspend fun joinRoom(inviteCode: String, friendUserId: String): Result<Unit>
    fun observeCurrentRoom(roomId: String): Flow<PairingRoom?>
    suspend fun checkUserHasActiveRoom(userId: String): Result<String?>
}

class PairingRepositoryImpl@Inject constructor(
    private val remoteDataSource: PairingRemoteDataSource,
    private val localDataSource: PairingLocalDataSource
) : PairingRepository {
    override fun createRoom(hostUserId: String): Flow<Result<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun joinRoom(
        inviteCode: String,
        friendUserId: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun observeCurrentRoom(roomId: String): Flow<PairingRoom?> {
        TODO("Not yet implemented")
    }

    override suspend fun checkUserHasActiveRoom(userId: String): Result<String?> {
        TODO("Not yet implemented")
    }

}

