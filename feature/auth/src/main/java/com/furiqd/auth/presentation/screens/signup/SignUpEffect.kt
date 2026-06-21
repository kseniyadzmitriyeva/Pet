package com.furiqd.auth.presentation.screens.signup

sealed interface SignUpEffect {
    data object NavigateBack : SignUpEffect
    data class NavigateToWelcome(val isOnboardingCompleted: Boolean) : SignUpEffect
    data object NavigateToSignIn : SignUpEffect
    data class ShowError(val message: String) : SignUpEffect
}
