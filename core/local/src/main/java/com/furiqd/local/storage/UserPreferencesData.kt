package com.furiqd.local.storage

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferencesData(
    val id: String,
    val name: String,
    val avatarId: Int?,
    val isOnboardingCompleted: Boolean
)