package com.furiqd.data.models

sealed interface UserSessionState {
    object Loading : UserSessionState
    object NotAuthorized : UserSessionState
    object NeedsOnboarding : UserSessionState
    object FullyAuthorized : UserSessionState
}