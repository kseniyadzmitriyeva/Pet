package com.furiqd.pairing.presentation.screen.createroom

sealed interface CreateRoomEffect {
    data class ShowToast(val message: String) : CreateRoomEffect
    data class NavigateToHost(val roomId: String) : CreateRoomEffect
    object NavigateToMain : CreateRoomEffect
}