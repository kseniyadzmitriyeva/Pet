package com.furiqd.designsystem.config

import com.furiqd.designsystem.R
import com.furiqd.domain.model.PetEggType

object PetEggConfig {
    fun getDrawableByType(type: PetEggType): Int {
        return when (type) {
            PetEggType.GREEN -> R.drawable.green_egg
            PetEggType.BLUE -> R.drawable.blue_egg
            PetEggType.ORANGE -> R.drawable.orange_egg
            PetEggType.PURPLE -> R.drawable.purple_egg
        }
    }
}