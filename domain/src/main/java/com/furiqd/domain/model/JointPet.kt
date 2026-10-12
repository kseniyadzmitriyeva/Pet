package com.furiqd.domain.model

import java.util.Date

data class JointPet(
    val id: String = "",
    val avatarId: Int = 0,
    val name: String = "Egg",
    val level: Int = 1,
    val experience: Int = 0,
    val stage: String = "EGG",
    val hatchedAt: Date? = null,
    val petEggType: PetEggType = PetEggType.GREEN
)