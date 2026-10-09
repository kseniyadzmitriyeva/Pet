package com.furiqd.home.presentation.screen.profile

data class ProfileState (
    val user: UserProfile? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

data class UserProfile(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val amountOfRooms: Int = 0
)