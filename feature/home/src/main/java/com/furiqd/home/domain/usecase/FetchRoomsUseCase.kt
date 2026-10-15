package com.furiqd.home.domain.usecase

import com.furiqd.shared.domain.model.PairingRoomWithDetails
import com.furiqd.shared.domain.repository.RoomRepository
import com.furiqd.shared.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchRoomsUseCase @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: String): Flow<List<PairingRoomWithDetails>> {
        return roomRepository.observeUserRooms(userId).map { rooms ->
            coroutineScope {
                rooms.map { room ->
                    async {
                        val host = userRepository.getUserById(room.hostId)
                        val guest = room.friendId?.let { userRepository.getUserById(it) }

                        PairingRoomWithDetails(
                            room = room,
                            hostAvatarUrl = host?.avatarId,
                            guestAvatarUrl = guest?.avatarId,
                            hostName = host?.name,
                            guestName = guest?.name
                        )
                    }
                }.awaitAll()
            }
        }
    }
}