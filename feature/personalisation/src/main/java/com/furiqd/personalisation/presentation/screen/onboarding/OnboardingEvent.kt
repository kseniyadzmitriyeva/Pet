package com.furiqd.personalisation.presentation.screen.onboarding

sealed interface OnboardingEvent {
    data class NicknameChanged(val nickname: String) : OnboardingEvent
    data class NameChanged(val name: String) : OnboardingEvent
    data class AvatarSelected(val avatarId: Int) : OnboardingEvent
    object NextClicked : OnboardingEvent
}