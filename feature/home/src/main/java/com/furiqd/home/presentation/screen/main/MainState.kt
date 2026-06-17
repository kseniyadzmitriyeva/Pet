package com.furiqd.home.presentation.screen.main

import androidx.compose.runtime.Immutable

@Immutable
data class MainState(
    val isLoading: Boolean = false,
    val userAvatarId: Int? = 1,
    val friendAvatarId: Int? = 4,
    val isStreakActive: Boolean = true,
    val streakCount: Int = 12,
    val isActiveHour: Boolean = true,
    val timeLeft: String = "42:15",
    val userTask: String = "Green & Round",
    val friendTask: String = "Blue & Square",
    val isUserPhotoDone: Boolean = false,
    val isFriendPhotoDone: Boolean = true,
    val compositeImageUri: String? = null  
)