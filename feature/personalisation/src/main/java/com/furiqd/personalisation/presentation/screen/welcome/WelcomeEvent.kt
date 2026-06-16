package com.furiqd.personalisation.presentation.screen.welcome

sealed interface WelcomeEvent {
    object onGetStartedClicked : WelcomeEvent
}