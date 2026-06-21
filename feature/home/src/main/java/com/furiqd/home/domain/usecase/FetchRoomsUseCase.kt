package com.furiqd.home.domain.usecase

import com.furiqd.domain.repository.RoomRepository
import com.furiqd.home.presentation.screen.main.RoomUiModel
import javax.inject.Inject

class FetchRoomsUseCase@Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(roomId: String): RoomUiModel {
        return roomRepository.fetchRoom(roomId)
    }
}