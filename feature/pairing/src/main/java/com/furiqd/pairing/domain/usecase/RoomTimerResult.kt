package com.furiqd.pairing.domain.usecase

sealed interface RoomTimerResult {
    data class Active(val formattedTime: String, val millisLeft: Long) : RoomTimerResult
    data object Expired : RoomTimerResult
}