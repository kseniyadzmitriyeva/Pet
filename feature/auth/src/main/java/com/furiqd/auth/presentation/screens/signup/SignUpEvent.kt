package com.furiqd.auth.presentation.screens.signup

sealed interface SignUpEvent{
    data class EmailChanged(val email: String) : SignUpEvent
    data class PasswordChanged(val password: String) : SignUpEvent
    data class PrivacyChecked(val isChecked: Boolean) : SignUpEvent
    object GoogleClicked : SignUpEvent
    object SignUpClicked : SignUpEvent
    object BackClicked : SignUpEvent
    object GoToSignInClicked: SignUpEvent
}