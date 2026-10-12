package com.furiqd.personalisation.presentation.screen.onboarding

interface OnboardingEffect {
    object NavigateToNext : OnboardingEffect
    data class ShowError(val message: String) : OnboardingEffect
    data object UploadAvatar : OnboardingEffect
}