package com.furiqd.home.presentation.screen.profile

interface ProfileEffect {
    data object NavigateToMain : ProfileEffect
    data object NavigateToAuth : ProfileEffect
    data object NavigateToEditProfile : ProfileEffect
    data object AddAvatar: ProfileEffect
    data class ShowToast(val message: String) : ProfileEffect
}