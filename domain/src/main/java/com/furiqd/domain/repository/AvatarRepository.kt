package com.furiqd.domain.repository

import android.net.Uri
import com.furiqd.domain.model.Avatar
import kotlinx.coroutines.flow.Flow

interface AvatarRepository {
    fun getAvailableAvatars(): Flow<List<Avatar>>
    suspend fun getAvatarById(id: Int?): Avatar
    suspend fun saveAvatar(avatarBytes: ByteArray): Result<Unit>
}