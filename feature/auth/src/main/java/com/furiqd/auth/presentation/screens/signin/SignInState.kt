package com.furiqd.auth.presentation.screens.signin

import com.furiqd.auth.domain.model.ValidationError

data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailError: ValidationError.Email? = null,
    val passwordError: ValidationError.Password? = null,
    val isLoading: Boolean = false,
    val isSignInEnabled: Boolean = false
)