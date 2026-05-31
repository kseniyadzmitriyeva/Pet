package com.furiqd.auth.presentation.start

sealed interface StartEffect {
    object NavigateToSignUp : StartEffect
    object NavigateToSignIn : StartEffect
    data class ShowError(val message: String) : StartEffect
}