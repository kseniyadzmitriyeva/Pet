package com.furiqd.domain.model

data class User(
    val id: String,
    val name: String,
    val avatarId: Int?,
    val isOnboardingCompleted: Boolean
)