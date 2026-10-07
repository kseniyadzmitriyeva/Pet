package com.furiqd.home.presentation.screen.createroom

sealed interface CreateRoomEffect {
    data class ShowToast(val message: String) : CreateRoomEffect
    object NavigateToMain : CreateRoomEffect
}