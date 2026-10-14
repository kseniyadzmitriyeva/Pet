package com.furiqd.pairing.presentation.screen.hostroom

sealed interface HostRoomEffect {
    data object ShowCopiedToast : HostRoomEffect
    data object NavigateBack : HostRoomEffect
}