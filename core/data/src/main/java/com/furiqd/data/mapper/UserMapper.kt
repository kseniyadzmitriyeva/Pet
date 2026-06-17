package com.furiqd.data.mapper

import com.furiqd.domain.model.User
import com.furiqd.network.dto.UserDto

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        avatarId = this.avatarId,
        isOnboardingCompleted = this.isOnboardingCompleted
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id,
        name = this.name,
        avatarId = this.avatarId,
        isOnboardingCompleted = this.isOnboardingCompleted
    )
}