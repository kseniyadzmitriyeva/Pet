package com.furiqd.auth.presentation.start

sealed interface StartEvent {
    object SignUpClicked : StartEvent
    object SignInClicked : StartEvent
}