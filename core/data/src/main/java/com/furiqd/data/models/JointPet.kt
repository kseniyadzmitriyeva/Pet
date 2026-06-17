package com.furiqd.data.models

import java.util.Date

data class JointPet(
    val id: String = "",
    val name: String = "Egg",
    val level: Int = 1,
    val experience: Int = 0,
    val stage: String = "EGG",
    val hatchedAt: Date? = null
)