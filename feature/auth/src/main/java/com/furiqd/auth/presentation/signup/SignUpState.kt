package com.furiqd.auth.presentation.signup

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isPrivacyChecked: Boolean = false,
    val isLoading: Boolean = false,
    val isSignUpEnabled: Boolean = false
)