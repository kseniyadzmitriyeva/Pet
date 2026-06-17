package com.furiqd.network.dto

import com.google.firebase.firestore.DocumentId

data class UserDto(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val avatarId: Int? = null,
    val isOnboardingCompleted: Boolean = false
)