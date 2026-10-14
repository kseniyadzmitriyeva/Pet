package com.furiqd.shared.model

import java.util.Calendar
import java.util.Date

data class PairingRoom(
    val id: String = "",
    val code: String = "",
    val hostId: String = "",
    val friendId: String? = null,
    val pet: JointPet = JointPet(),
    val createdAt: Date = Date(),
    val status: RoomStatus = RoomStatus.NotFound
){
    val isExpired: Boolean
        get() {
            val cal = Calendar.getInstance().apply { time = createdAt }
            cal.add(Calendar.HOUR, 24)
            return Date().after(cal.time)
        }
}