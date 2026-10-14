package com.furiqd.shared.model

data class AuthResult (
    val token: String,
    val email: String,
    val name: String?
)