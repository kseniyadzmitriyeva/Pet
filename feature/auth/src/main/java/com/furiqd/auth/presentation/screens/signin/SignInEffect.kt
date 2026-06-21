package com.furiqd.auth.presentation.screens.signin

sealed interface SignInEffect {
    data object NavigateBack : SignInEffect
    data class NavigateToWelcome(val isOnboardingCompleted: Boolean) : SignInEffect
    data class ShowError(val message: String) : SignInEffect
    data object NavigateToForgotPassword : SignInEffect
    data object NavigateToSignUp : SignInEffect
}
