package com.furiqd.shared.domain.model

enum class PetEggType(val id: Int) {
    GREEN(1),
    BLUE(2),
    ORANGE(3),
    PURPLE(4);

    companion object {
        fun fromId(id: Int): PetEggType = entries.find { it.id == id } ?: GREEN
    }
}