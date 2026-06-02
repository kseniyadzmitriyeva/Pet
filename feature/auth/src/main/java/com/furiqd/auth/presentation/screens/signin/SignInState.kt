package com.furiqd.auth.presentation.screens.signin

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSignInEnabled: Boolean = false
)