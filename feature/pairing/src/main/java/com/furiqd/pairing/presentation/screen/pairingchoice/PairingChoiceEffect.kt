package com.furiqd.pairing.presentation.screen.pairingchoice

sealed interface PairingChoiceEffect {
    data object NavigateToCreateRoom : PairingChoiceEffect
    data object NavigateToJoinRoom : PairingChoiceEffect
    data object Close : PairingChoiceEffect
    data class ShowError(val message: String) : PairingChoiceEffect
}