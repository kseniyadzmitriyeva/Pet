package com.furiqd.data.mapper

import com.furiqd.shared.model.User
import com.furiqd.local.entity.LocalUserEntity
import com.furiqd.network.dto.UserDto

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        avatarId = this.avatarId,
        isOnboardingCompleted = this.isOnboardingCompleted,
        roomIds = this.roomIds,
        email = this.email,
        nickName = this.nickName
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id,
        name = this.name,
        avatarId = this.avatarId,
        isOnboardingCompleted = this.isOnboardingCompleted,
        roomIds = this.roomIds,
        email = this.email,
        nickName = this.nickName
    )
}

fun User.toLocal(): LocalUserEntity {
    return LocalUserEntity(
        id = this.id,
        name = this.name,
        avatarId = this.avatarId,
        isOnboardingCompleted = this.isOnboardingCompleted,
        roomIds = RoomConverters().fromStringList(this.roomIds),
        email = this.email,
        nickName = this.nickName
    )
}

fun LocalUserEntity.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        avatarId = this.avatarId,
        isOnboardingCompleted = this.isOnboardingCompleted,
        roomIds = RoomConverters().toStringList(this.roomIds) ?: emptyList(),
        email = this.email,
        nickName = this.nickName
    )
}