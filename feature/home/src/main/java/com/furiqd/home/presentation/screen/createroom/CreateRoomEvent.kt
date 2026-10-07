package com.furiqd.home.presentation.screen.createroom

sealed interface CreateRoomEvent {
    data class CreateRoomClicked(val name: String, val avatarId: Int) : CreateRoomEvent
    object DismissDialog : CreateRoomEvent
    data class NameChanged(val name: String) : CreateRoomEvent
    object NextEggClicked : CreateRoomEvent
    object PreviousEggClicked : CreateRoomEvent
}