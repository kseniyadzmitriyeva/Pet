package com.furiqd.auth.presentation.screens.welcome

sealed interface WelcomeEffect {
    object NavigateToHome : WelcomeEffect
    data class ShowError(val message: String) : WelcomeEffect
}