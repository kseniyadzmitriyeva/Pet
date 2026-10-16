package com.furiqd.home.presentation.screen.main

sealed interface RoomUiModel {
    val id: String
    val isSelected: Boolean

    data class WaitingForFriend(
        override val id: String,
        override val isSelected: Boolean = false,
        val inviteCode: String,
        val petName: String,
        val petAvatarId: Int,
        val expiringTime: Long,
        val userAvatarUrl: String? = null,
        val friendAvatarUrl: String? = null
    ) : RoomUiModel

    data class Active(
        override val id: String,
        override val isSelected: Boolean = false,
        val name: String,
        val petName: String,
        val petAvatarId: Int,
        val userAvatarUrl: String?,
        val friendAvatarUrl: String?,
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