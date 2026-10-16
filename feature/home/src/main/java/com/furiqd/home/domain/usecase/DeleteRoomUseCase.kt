package com.furiqd.home.domain.usecase

import com.furiqd.home.domain.repository.HomeRepository
import javax.inject.Inject

internal class DeleteRoomUseCase@Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(roomId: String) {
        repository.deleteRoom(roomId)
    }
}