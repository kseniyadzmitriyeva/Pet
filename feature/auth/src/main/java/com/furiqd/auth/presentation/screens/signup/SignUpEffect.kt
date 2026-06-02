package com.furiqd.auth.presentation.screens.signup

sealed interface SignUpEffect {
    object NavigateBack : SignUpEffect
    object NavigateToWelcome : SignUpEffect
    object NavigateToSignIn : SignUpEffect
    data class ShowError(val message: String) : SignUpEffect
}