package com.furiqd.network.dto

data class CreateRoomRequestDto(
    val hostUserId: String,
    val uniqueCode: String,
    val petName: String,
    val petEggType: Int
)