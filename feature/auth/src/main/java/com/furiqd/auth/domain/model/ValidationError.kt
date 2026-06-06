package com.furiqd.auth.domain.model

sealed interface ValidationError {
    sealed interface Email : ValidationError {
        object Empty : Email
        object Invalid : Email
    }
    sealed interface Password : ValidationError {
        object Empty : Password
        object TooShort : Password
        object InvalidCharacters : Password
    }
}