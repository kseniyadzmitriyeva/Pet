package com.furiqd.pairing.data.datasource

import com.furiqd.pairing.data.datasource.model.dto.JointPetDto
import com.furiqd.pairing.data.datasource.model.dto.PairingRoomDto
import com.furiqd.pairing.data.datasource.model.RoomConstants
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID
import javax.inject.Inject
interface PairingRemoteDataSource {
    suspend fun createRoom(newRoomDto: PairingRoomDto): String

    suspend fun executeJoinTransaction(inviteCode: String, friendUserId: String): String

    suspend fun getPairingRoom(roomId: String): PairingRoomDto?

    fun observeRoom(roomId: String): Flow<PairingRoomDto?>
    suspend fun isCodeExisting(code: String): Boolean
}

internal class FirebasePairingDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : PairingRemoteDataSource {

    private val roomsCollection = firestore.collection(RoomConstants.COLLECTION_ROOMS)
    private val usersCollection = firestore.collection(RoomConstants.COLLECTION_USERS)

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
        val userRef = usersCollection.document(newRoomDto.hostId)
        userRef.update("roomIds",
            FieldValue.arrayUnion(documentRef.id)).await()
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
        val userRef = usersCollection.document(friendUserId)

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
            transaction.update(userRef, "roomIds",
                FieldValue.arrayUnion(document.id))
            document.id
        }.await()
    }

    override suspend fun getPairingRoom(roomId: String): PairingRoomDto? {
        val snapshot = roomsCollection.document(roomId).get().await()
        return if (snapshot.exists()) snapshot.toObject<PairingRoomDto>() else null
    }

    override fun observeRoom(roomId: String): Flow<PairingRoomDto?> {
        return roomsCollection.document(roomId)
            .snapshots()
            .map { snapshot ->
                if (snapshot.exists()) snapshot.toObject<PairingRoomDto>() else null
            }
    }
}