package com.furiqd.pairing.data.mapper

import com.furiqd.shared.domain.model.PairingRoom
import com.furiqd.shared.domain.model.RoomStatus
import com.furiqd.pairing.data.datasource.model.dto.PairingRoomDto

fun PairingRoomDto.toDomain(): PairingRoom {
    val domainStatus = when (this.status.uppercase()) {
        "WAITING" -> RoomStatus.WaitingForFriend
        "ACTIVE" -> RoomStatus.Active
        "EXPIRED" -> RoomStatus.Expired
        else -> RoomStatus.NotFound
    }

    return PairingRoom(
        id = this.id,
        code = this.code,
        hostId = this.hostId,
        friendId = this.friendId,
        pet = this.pet.toDomain(),
        createdAt = this.createdAt,
        status = domainStatus
    )
}

fun PairingRoom.toDto(): PairingRoomDto {
    val stringStatus = when (this.status) {
        is RoomStatus.WaitingForFriend -> "WAITING"
        is RoomStatus.Active -> "ACTIVE"
        is RoomStatus.Expired -> "EXPIRED"
        is RoomStatus.NotFound -> "NOT_FOUND"
    }

    return PairingRoomDto(
        id = this.id,
        code = this.code,
        hostId = this.hostId,
        friendId = this.friendId,
        pet = this.pet.toDto(),
        createdAt = this.createdAt,
        status = stringStatus
    )
}