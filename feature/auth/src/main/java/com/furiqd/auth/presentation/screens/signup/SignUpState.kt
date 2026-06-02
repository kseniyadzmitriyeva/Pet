package com.furiqd.auth.presentation.screens.signup

data class SignUpState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isPrivacyChecked: Boolean = false,
    val isLoading: Boolean = false,
    val isSignUpEnabled: Boolean = false
)