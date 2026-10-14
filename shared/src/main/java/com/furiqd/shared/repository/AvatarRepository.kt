package com.furiqd.shared.repository

import com.furiqd.shared.model.Avatar
import kotlinx.coroutines.flow.Flow

interface AvatarRepository {
    fun getAvailableAvatars(): Flow<List<Avatar>>
    suspend fun getAvatarById(id: Int?): Avatar
    suspend fun saveAvatar(avatarBytes: ByteArray): Result<Unit>
}