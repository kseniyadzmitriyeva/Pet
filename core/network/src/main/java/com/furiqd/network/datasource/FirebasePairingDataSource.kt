package com.furiqd.network.datasource

import com.furiqd.network.contract.RemotePairingDataSource
import com.furiqd.network.dto.PairingRoomDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirebasePairingDataSource @Inject constructor(
    val firestore: FirebaseFirestore
) : RemotePairingDataSource {

    private val roomsCollection = firestore.collection("rooms")

    override fun observeRoom(roomId: String): Flow<PairingRoomDto?> {
        return roomsCollection.document(roomId)
            .snapshots()
            .map { snapshot ->
                if (snapshot.exists()) {
                    snapshot.toObject<PairingRoomDto>()
                } else {
                    null
                }
            }
    }

    override suspend fun sendNudge(roomId: String, senderUserId: String) {
        roomsCollection.document(roomId)
            .update(
                "lastNudgeBy", senderUserId,
                "lastNudgeTimestamp", FieldValue.serverTimestamp()
            )
            .await()
    }

    override suspend fun getRoomDetails(roomId: String): PairingRoomDto? {
        val snapshot = roomsCollection.document(roomId).get().await()
        return if (snapshot.exists()) snapshot.toObject<PairingRoomDto>() else null
    }
}