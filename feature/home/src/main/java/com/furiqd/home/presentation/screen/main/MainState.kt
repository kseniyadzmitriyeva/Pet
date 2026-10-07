package com.furiqd.home.presentation.screen.main

import androidx.compose.runtime.Immutable
import com.furiqd.designsystem.R
import com.furiqd.domain.model.User

sealed interface RoomUiModel {
    val id: String
    val isSelected: Boolean

    data class Active(
        override val id: String,
        override val isSelected: Boolean = false,
        val name: String,
        val inviteCode: String,
        val petName: String,
        val petAvatarId: Int,
        val isPartnerConnected: Boolean,

        val userAvatarId: Int = R.drawable.avatar_boy_svgrepo_com__1_,
        val friendAvatarId: Int = R.drawable.avatar_boy_svgrepo_com__4_,
        val isStreakActive: Boolean = true,
        val streakCount: Int = 12,
        val isActiveHour: Boolean = true,
        val timeLeft: String = "42:15",
        val userTask: String = "Green & Round",
        val friendTask: String = "Blue & Square",
        val isUserPhotoDone: Boolean = false,
        val isFriendPhotoDone: Boolean = true,
        val compositeImageUri: String? = null
    ) : RoomUiModel

    data class Placeholder(
        override val id: String = "placeholder_id",
        override val isSelected: Boolean = false
    ) : RoomUiModel
}

@Immutable
data class MainState(
    val isLoading: Boolean = false,
    val rooms: List<RoomUiModel> = emptyList(),
    val errorMessage: String? = null,
    val userId: String = ""
) {
    val selectedRoom: RoomUiModel?
        get() = rooms.find { it.isSelected }

    private val selectedIndex: Int
        get() = rooms.indexOf(selectedRoom)

    val previousRoom: RoomUiModel?
        get() = if (selectedIndex > 0) rooms[selectedIndex - 1] else null

    val nextRoom: RoomUiModel?
        get() = if (selectedIndex in 0 until rooms.lastIndex)
            rooms[selectedIndex + 1] else null
}