package com.furiqd.domain.model

data class AuthResult (
    val token: String,
    val email: String,
    val name: String?
)