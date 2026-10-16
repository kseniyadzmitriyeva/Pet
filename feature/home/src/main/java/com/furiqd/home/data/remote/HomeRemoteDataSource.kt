package com.furiqd.home.data.remote

import com.furiqd.network.dto.PairingRoomDto
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal interface HomeRemoteDataSource {
    fun observeUserRooms(userId: String): Flow<List<PairingRoomDto>>
    suspend fun deleteRoom(roomId: String)
}

internal class HomeRemoteDataSourceImpl@Inject constructor(
    private val firestore: FirebaseFirestore
) : HomeRemoteDataSource {

    private val roomsCollection = firestore.collection("rooms")

    override fun observeUserRooms(userId: String): Flow<List<PairingRoomDto>> {
        return roomsCollection
            .where(
                Filter.or(
                    Filter.equalTo("hostId", userId),
                    Filter.equalTo("friendId", userId)
                )
            )
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.documents.mapNotNull { it.toObject<PairingRoomDto>() }
            }
    }

    override suspend fun deleteRoom(roomId: String) {
        roomsCollection.document(roomId).delete().await()
    }

}