package com.furiqd.pairing.presentation.screen.joinroom

sealed interface JoinRoomEffect {
    data class NavigateToRoom(val roomId: String) : JoinRoomEffect
    data class ShowToast(val message: String) : JoinRoomEffect
    data object NavigateBack : JoinRoomEffect
}