package com.furiqd.shared.model

data class PairingRoomWithDetails(
    val room: PairingRoom,
    val hostAvatarUrl: String?,
    val guestAvatarUrl: String?,
    val hostName: String?,
    val guestName: String?
)