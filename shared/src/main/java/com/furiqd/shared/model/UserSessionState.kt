package com.furiqd.shared.model

sealed interface UserSessionState {
    data object Loading : UserSessionState
    data object NotAuthorized : UserSessionState
    data object NeedsOnboarding : UserSessionState
    data object FullyAuthorized : UserSessionState
}