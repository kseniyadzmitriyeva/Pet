package com.furiqd.pairing.presentation.screen.pairingchoice

sealed interface PairingChoiceEvent {
    data object OnCreateRoomClicked : PairingChoiceEvent
    data object OnJoinRoomClicked : PairingChoiceEvent
    data object OnDismiss : PairingChoiceEvent
}