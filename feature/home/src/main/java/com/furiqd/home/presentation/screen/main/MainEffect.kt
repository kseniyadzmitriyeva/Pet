package com.furiqd.home.presentation.screen.main

sealed interface MainEffect {
    data class CopyToClipboard(val text: String) : MainEffect
    data class ShowToast(val message: String) : MainEffect
    object NavigateToProfile : MainEffect
    object NavigateToAuth : MainEffect
    object TriggerPetAnimation : MainEffect
    object NavigateToCreateRoom : MainEffect
    object LeaveRoom : MainEffect
}