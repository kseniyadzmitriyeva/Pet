package com.furiqd.home.data.repository

import com.furiqd.home.data.remote.HomeRemoteDataSource
import com.furiqd.home.domain.repository.HomeRepository
import com.furiqd.shared.data.mapper.toDomain
import com.furiqd.shared.domain.model.PairingRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class HomeRepositoryImpl@Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource
): HomeRepository {
    override fun observeUserRooms(userId: String): Flow<List<PairingRoom>> {
        return remoteDataSource.observeUserRooms(userId)
            .map { dtoList ->
                dtoList.map { it.toDomain() }
            }
    }

    override suspend fun deleteRoom(roomId: String) {
        remoteDataSource.deleteRoom(roomId)
    }
}