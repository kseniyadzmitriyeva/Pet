package com.furiqd.shared.model

data class User(
    val id: String,
    val name: String,
    val avatarId: String?,
    val isOnboardingCompleted: Boolean,
    val roomIds: List<String>,
    val email: String?,
    val nickName: String?
)