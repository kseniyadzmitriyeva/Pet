package com.furiqd.domain.model

import java.util.Date

data class PairingRoom(
    val id: String = "",
    val code: String = "",
    val hostId: String = "",
    val friendId: String? = null,
    val pet: JointPet = JointPet(),
    val createdAt: Date = Date(),
    val status: RoomStatus = RoomStatus.WaitingForFriend
)