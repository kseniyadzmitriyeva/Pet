package com.furiqd.auth.presentation.screens.start

sealed interface StartEffect {
    object NavigateToSignUp : StartEffect
    object NavigateToSignIn : StartEffect
    data class ShowError(val message: String) : StartEffect
}