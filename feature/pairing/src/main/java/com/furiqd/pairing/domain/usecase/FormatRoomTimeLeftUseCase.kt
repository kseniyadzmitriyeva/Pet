package com.furiqd.pairing.domain.usecase

import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FormatRoomTimeLeftUseCase @Inject constructor() {
    operator fun invoke(createdAt: Date): RoomTimerResult {
        val expireCalendar = Calendar.getInstance().apply {
            time = createdAt
            add(Calendar.HOUR, 24)
        }
        val diff = expireCalendar.timeInMillis - System.currentTimeMillis()

        if (diff <= 0L) return RoomTimerResult.Expired

        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60

        val formatted = if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }

        return RoomTimerResult.Active(formattedTime = formatted, millisLeft = diff)
    }
}