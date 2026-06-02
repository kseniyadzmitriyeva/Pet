package com.furiqd.auth.presentation.screens.welcome

sealed interface WelcomeEvent {
    object onGetStartedClicked : WelcomeEvent
}