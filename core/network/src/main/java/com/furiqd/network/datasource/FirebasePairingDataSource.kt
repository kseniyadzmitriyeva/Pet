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
import java.util.Date
import javax.inject.Inject

class FirebasePairingDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemotePairingDataSource {

    private val roomsCollection = firestore.collection("rooms")

    override suspend fun isCodeExisting(code: String): Boolean {
        val querySnapshot = roomsCollection
            .whereEqualTo("code", code)
            .whereIn("status", listOf("WAITING", "ACTIVE"))
            .get()
            .await()

        return !querySnapshot.isEmpty
    }

    override suspend fun createRoom(newRoomDto: PairingRoomDto): String {
        val documentRef = roomsCollection.add(newRoomDto).await()
        return documentRef.id
    }

    override suspend fun executeJoinTransaction(inviteCode: String, friendUserId: String): String {
        val querySnapshot = roomsCollection
            .whereEqualTo("code", inviteCode)
            .whereEqualTo("status", "WAITING")
            .limit(1)
            .get()
            .await()

        if (querySnapshot.isEmpty) {
            throw NoSuchElementException("Error: room not found or exists")
        }

        val document = querySnapshot.documents.first()
        val roomRef = document.reference

        return firestore.runTransaction<String> { transaction ->
            val freshSnapshot = transaction.get(roomRef)
            val currentStatus = freshSnapshot.getString("status")

            if (currentStatus != "WAITING") {
                throw IllegalStateException("Room exists and occupied")
            }

            transaction.update(roomRef, "friendId", friendUserId)
            transaction.update(roomRef, "status", "ACTIVE")
            transaction.update(roomRef, "pet.stage", "BABY")
            transaction.update(roomRef, "pet.hatchedAt", Date())

            document.id
        }.await()
    }

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

    override suspend fun checkActiveRoom(userId: String): String? {
        val hostQuery = roomsCollection
            .whereEqualTo("hostId", userId)
            .whereEqualTo("status", "ACTIVE")
            .limit(1)
            .get()
            .await()

        if (!hostQuery.isEmpty) {
            return hostQuery.documents.first().id
        }

        val friendQuery = roomsCollection
            .whereEqualTo("friendId", userId)
            .whereEqualTo("status", "ACTIVE")
            .limit(1)
            .get()
            .await()

        if (!friendQuery.isEmpty) {
            return friendQuery.documents.first().id
        }

        return null
    }

    override suspend fun uploadUserPhoto(roomId: String, userId: String, imageBytes: ByteArray) {
        roomsCollection.document(roomId)
            .update("userPhotos.$userId", "url_from_storage_placeholder")
            .await()
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
}