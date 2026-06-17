package com.furiqd.data.mapper

import com.furiqd.domain.model.PairingRoom
import com.furiqd.domain.model.RoomStatus
import com.furiqd.network.dto.PairingRoomDto
import java.util.Calendar
import java.util.Date

fun PairingRoomDto.toDomain(): PairingRoom {
    val cal = Calendar.getInstance().apply { time = createdAt }
    cal.add(Calendar.HOUR, 24)
    val isExpired = Date().after(cal.time)

    val domainStatus = when {
        isExpired -> RoomStatus.Expired
        this.status == "ACTIVE" -> RoomStatus.Active
        else -> RoomStatus.WaitingForFriend
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
        is RoomStatus.Active -> "ACTIVE"
        else -> "WAITING"
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