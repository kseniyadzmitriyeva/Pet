package com.furiqd.personalisation.presentation.screen.welcome

sealed interface WelcomeEffect {
    object NavigateToNext : WelcomeEffect
    data class ShowError(val message: String) : WelcomeEffect
}