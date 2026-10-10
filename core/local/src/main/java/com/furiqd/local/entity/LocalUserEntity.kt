package com.furiqd.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class LocalUserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val avatarId: String?,
    val isOnboardingCompleted: Boolean,
    val roomIds: String?,
    val email: String?,
    val nickName: String?
)