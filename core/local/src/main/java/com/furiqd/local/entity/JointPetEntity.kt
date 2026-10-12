package com.furiqd.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "joint_pets")
data class JointPetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val level: Int,
    val experience: Int,
    val stage: String,
    val hatchedAt: Long?,
    val petEggType: Int
)
