package com.furiqd.pairing.data.datasource

import com.furiqd.pairing.data.datasource.model.dto.JointPetDto
import com.furiqd.pairing.data.datasource.model.dto.PairingRoomDto
import com.furiqd.pairing.data.datasource.model.RoomConstants
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
    suspend fun createRoom(
        hostUserId: String,
        uniqueCode: String,
        petName: String,
        petEggTypeId: Int
    ): String

    suspend fun joinRoom(
        inviteCode: String
    ): String

    suspend fun getPairingRoom(roomId: String): PairingRoomDto?

    fun observeRoom(roomId: String): Flow<PairingRoomDto?>
}

internal class FirebasePairingDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : PairingRemoteDataSource {

    private val roomsCollection = firestore.collection(RoomConstants.COLLECTION_ROOMS)

    override suspend fun createRoom(
        hostUserId: String,
        uniqueCode: String,
        petName: String,
        petEggTypeId: Int
    ): String {
        val newRoomDto = PairingRoomDto(
            code = uniqueCode,
            hostId = hostUserId,
            friendId = null,
            status = RoomConstants.Status.WAITING,
            createdAt = Date(),
            pet = JointPetDto(
                id = UUID.randomUUID().toString(),
                name = petName,
                petEggType = petEggTypeId,
                stage = RoomConstants.PetStage.EGG,
                level = RoomConstants.Defaults.INITIAL_LEVEL,
                experience = RoomConstants.Defaults.INITIAL_EXP,
                hatchedAt = null
            )
        )

        val documentRef = roomsCollection.add(newRoomDto).await()
        return documentRef.id
    }

    override suspend fun joinRoom(inviteCode: String): String {
        val querySnapshot = roomsCollection
            .whereEqualTo(RoomConstants.Fields.CODE, inviteCode)
            .whereEqualTo(RoomConstants.Fields.STATUS, RoomConstants.Status.WAITING)
            .limit(1)
            .get()
            .await()

        if (querySnapshot.isEmpty) {
            throw RoomException.RoomNotFoundException()
        }

        val document = querySnapshot.documents.first()
        val roomRef = document.reference

        firestore.runTransaction { transaction ->
            val freshSnapshot = transaction.get(roomRef)
            val currentStatus = freshSnapshot.getString(RoomConstants.Fields.STATUS)

            if (currentStatus != RoomConstants.Status.WAITING) {
                throw RoomException.RoomAlreadyOccupiedException()
            }

            transaction.update(roomRef, RoomConstants.Fields.STATUS, RoomConstants.Status.ACTIVE)
            transaction.update(roomRef, RoomConstants.Fields.PET_STAGE, RoomConstants.PetStage.BABY)
            transaction.update(roomRef, RoomConstants.Fields.PET_HATCHED_AT, Date())
        }.await()

        return document.id
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