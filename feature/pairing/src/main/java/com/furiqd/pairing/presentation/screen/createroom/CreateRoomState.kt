package com.furiqd.pairing.presentation.screen.createroom

import com.furiqd.shared.model.PetEggType

data class CreateRoomState (
    val petName : String = "",
    val petEggType : PetEggType = PetEggType.GREEN,
    val isLoading : Boolean = false,
    val errorMessage : String? = null
) {

    companion object {
        val availableEggs = PetEggType.entries
    }
}