package com.furiqd.personalisation.presentation.screen.onboarding

import androidx.compose.runtime.Immutable
import com.furiqd.domain.model.Avatar


@Immutable
data class OnboardingState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val name: String = "",
    val selectedAvatarId: Int? = null,
    val avatars: List<Avatar> = emptyList(),
    val isNicknameError: Boolean = false,
    val isNameError: Boolean = false,
    val isNextButtonEnabled: Boolean = false
)