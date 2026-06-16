package com.furiqd.personalisation.presentation.screen.welcome

import androidx.compose.runtime.Immutable

@Immutable
data class WelcomeUiState(
    val userDisplayName: String? = null,
    val isLoading: Boolean = false
)