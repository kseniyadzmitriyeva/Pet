package com.furiqd.domain.model

sealed interface UserSessionState {
    object Loading : UserSessionState
    object NotAuthorized : UserSessionState
    object NeedsOnboarding : UserSessionState
    object FullyAuthorized : UserSessionState
}