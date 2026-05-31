package com.furiqd.auth.presentation.signup

sealed interface SignUpEvent{
    data class NameChanged(val name: String) : SignUpEvent
    data class EmailChanged(val email: String) : SignUpEvent
    data class PasswordChanged(val password: String) : SignUpEvent
    data class PrivacyChecked(val isChecked: Boolean) : SignUpEvent
    object GoogleClicked : SignUpEvent
    object SignUpClicked : SignUpEvent
    object BackClicked : SignUpEvent
}