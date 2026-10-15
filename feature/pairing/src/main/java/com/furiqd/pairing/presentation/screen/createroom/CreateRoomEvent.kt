package com.furiqd.pairing.presentation.screen.createroom

import com.furiqd.shared.domain.model.PetEggType

sealed interface CreateRoomEvent {
    data class CreateRoomClicked(
        val petName: String,
        val petEggType: PetEggType
    ) : CreateRoomEvent
    object DismissDialog : CreateRoomEvent
    data class NameChanged(val name: String) : CreateRoomEvent
    object NextEggClicked : CreateRoomEvent
    object PreviousEggClicked : CreateRoomEvent
}