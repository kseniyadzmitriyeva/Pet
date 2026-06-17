package com.furiqd.personalisation.presentation.screen.onboarding

sealed interface OnboardingEffect {
    object NavigateToNext : OnboardingEffect
    data class ShowError(val message: String) : OnboardingEffect
}