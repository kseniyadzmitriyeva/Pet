package com.furiqd.shared.config

import com.furiqd.shared.R
import com.furiqd.shared.model.PetEggType

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