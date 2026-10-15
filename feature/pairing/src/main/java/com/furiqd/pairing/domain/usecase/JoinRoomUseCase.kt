package com.furiqd.pairing.domain.usecase

import com.furiqd.pairing.data.repository.PairingRepository
import javax.inject.Inject

class JoinRoomUseCase@Inject constructor(
    private val roomRepository: PairingRepository,
) {
    suspend operator fun invoke(code: String) = roomRepository.joinRoom(code)
}