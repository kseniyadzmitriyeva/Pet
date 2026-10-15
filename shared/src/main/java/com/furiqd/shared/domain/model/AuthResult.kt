package com.furiqd.shared.domain.model

data class AuthResult (
    val token: String,
    val email: String,
    val name: String?
)