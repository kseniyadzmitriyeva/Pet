package com.furiqd.auth.presentation.screens.signin

sealed interface SignInEvent{
    data class EmailChanged(val email: String) : SignInEvent
    data class PasswordChanged(val password: String) : SignInEvent
    object GoogleClicked : SignInEvent
    object SignInClicked : SignInEvent
    object BackClicked : SignInEvent
    object GoToSignUpClicked : SignInEvent
    object ForgotPasswordClicked : SignInEvent
}