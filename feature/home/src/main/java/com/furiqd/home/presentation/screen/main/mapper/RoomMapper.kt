package com.furiqd.home.presentation.screen.main.mapper

import com.furiqd.home.presentation.screen.main.RoomUiModel
import com.furiqd.shared.config.PetEggConfig
import com.furiqd.shared.domain.model.PairingRoomWithDetails
import com.furiqd.shared.domain.model.RoomStatus
import com.furiqd.shared.util.ImageUrlFormatter

fun PairingRoomWithDetails.toUiModel(
    isSelected: Boolean,
    imageFormatter: ImageUrlFormatter
): RoomUiModel {

    val formattedHostAvatar = imageFormatter.format(this.hostAvatarUrl)
    val formattedGuestAvatar = imageFormatter.format(this.guestAvatarUrl)

    return when (this.room.status) {
        is RoomStatus.WaitingForFriend -> RoomUiModel.WaitingForFriend(
            id = this.room.id,
            isSelected = isSelected,
            inviteCode = this.room.code,
            petName = this.room.pet.name,
            petAvatarId = PetEggConfig.getDrawableByType(this.room.pet.petEggType),
            expiringTime = this.room.createdAt.time,
            userAvatarUrl = formattedHostAvatar,
            friendAvatarUrl = formattedGuestAvatar
        )

        is RoomStatus.Active -> RoomUiModel.Active(
            id = this.room.id,
            isSelected = isSelected,
            name = "Room ${this.room.code}",
            petName = this.room.pet.name,
            petAvatarId = PetEggConfig.getDrawableByType(this.room.pet.petEggType),
            userAvatarUrl = formattedHostAvatar,
            friendAvatarUrl = formattedGuestAvatar
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