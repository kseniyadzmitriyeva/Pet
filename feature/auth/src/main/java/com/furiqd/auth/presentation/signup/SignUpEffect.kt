package com.furiqd.auth.presentation.signup

sealed interface SignUpEffect {
    object NavigateBack : SignUpEffect
    object NavigateToWelcome : SignUpEffect
    data class ShowError(val message: String) : SignUpEffect
}