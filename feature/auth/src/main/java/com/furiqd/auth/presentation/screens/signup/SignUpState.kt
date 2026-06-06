package com.furiqd.auth.presentation.screens.signup

import com.furiqd.auth.domain.model.ValidationError

data class SignUpState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val emailError: ValidationError.Email? = null,
    val passwordError: ValidationError.Password? = null,
    val isPrivacyChecked: Boolean = false,
    val isLoading: Boolean = false,
    val isSignUpEnabled: Boolean = false
)