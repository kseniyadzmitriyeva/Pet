package com.furiqd.auth.presentation.screens.signin
sealed interface SignInEffect {
    object NavigateBack : SignInEffect
    object NavigateToWelcome : SignInEffect
    data class ShowError(val message: String) : SignInEffect
    object NavigateToForgotPassword : SignInEffect
    object NavigateToSignUp: SignInEffect
}