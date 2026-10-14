package com.furiqd.pairing.presentation.screen.hostroom

sealed interface HostRoomEvent {
    object OnCopyCodeClicked : HostRoomEvent
    object OnShareClicked : HostRoomEvent
    object OnBackClicked : HostRoomEvent
}