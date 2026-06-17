package com.furiqd.network.dto

import java.util.Date

data class JointPetDto(
    val id: String = "",
    val name: String = "Egg",
    val level: Int = 1,
    val experience: Int = 0,
    val stage: String = "EGG",
    val hatchedAt: Date? = null
)