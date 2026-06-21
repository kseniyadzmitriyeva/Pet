package com.furiqd.network.dto

data class AuthResultDto(
    val token: String,
    val email: String,
    val name: String?
)