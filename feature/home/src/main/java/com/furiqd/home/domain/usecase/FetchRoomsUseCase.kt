package com.furiqd.home.domain.usecase

import com.furiqd.domain.repository.RoomRepository
import com.furiqd.home.presentation.screen.main.RoomUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchRoomsUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    operator fun invoke(userId: String): Flow<List<RoomUiModel>> {
        return roomRepository.observeUserRooms(userId).map { rooms ->
            rooms.map { room ->
                RoomUiModel.Active(
                    id = room.id,
                    name = room.pet.name,
                    inviteCode = room.code,
                    petName = room.pet.name,
                    petAvatarId = room.pet.avatarId,
                    isPartnerConnected = room.friendId != null
                )
            }
        }
    }
}