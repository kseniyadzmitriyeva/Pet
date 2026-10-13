package com.furiqd.home.presentation.screen.main.mapper

import com.furiqd.domain.model.RoomStatus
import com.furiqd.home.presentation.screen.main.RoomUiModel
import com.furiqd.designsystem.config.PetEggConfig
import com.furiqd.domain.model.PairingRoomWithDetails

fun PairingRoomWithDetails.toUiModel(isSelected: Boolean): RoomUiModel {

    return when (this.room.status) {
        is RoomStatus.WaitingForFriend -> RoomUiModel.WaitingForFriend(
            id = this.room.id,
            isSelected = isSelected,
            inviteCode = this.room.code,
            petName = this.room.pet.name,
            petAvatarId = PetEggConfig.getDrawableByType(this.room.pet.petEggType),
            expiringTime = this.room.createdAt.time,
            userAvatarUrl = this.hostAvatarUrl,
            friendAvatarUrl = this.guestAvatarUrl
        )
        is RoomStatus.Active -> RoomUiModel.Active(
            id = this.room.id,
            isSelected = isSelected,
            name = "Room ${this.room.code}",
            petName = this.room.pet.name,
            petAvatarId = PetEggConfig.getDrawableByType(this.room.pet.petEggType),
            userAvatarUrl = null,
            friendAvatarUrl = null,
        )
        is RoomStatus.Expired -> RoomUiModel.Expired(
            id = this.room.id,
            isSelected = isSelected
        )
        is RoomStatus.NotFound -> RoomUiModel.Placeholder(
            id = this.room.id,
            isSelected = isSelected
        )
    }
}