package com.furiqd.data.repositories

import com.furiqd.shared.model.Avatar
import com.furiqd.shared.model.AvatarType
import com.furiqd.shared.repository.AvatarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AvatarRepositoryImpl @Inject constructor(
) : AvatarRepository {

    private val builtInAvatars = listOf(
        Avatar(id = 1, name = "Boy 1", type = AvatarType.Resource(1)),
        Avatar(id = 2, name = "Boy 2", type = AvatarType.Resource(2)),
        Avatar(id = 3, name = "Boy 3", type = AvatarType.Resource(3)),
        Avatar(id = 4, name = "Girl 1", type = AvatarType.Resource(4)),
        Avatar(id = 5, name = "Girl 2", type = AvatarType.Resource(5)),
        Avatar(id = 6, name = "Girl 2", type = AvatarType.Resource(6))
    )

    override fun getAvailableAvatars(): Flow<List<Avatar>> {
        return flowOf(builtInAvatars)
    }

    override suspend fun getAvatarById(id: Int?): Avatar {
        TODO("Not yet implemented")
    }

    override suspend fun saveAvatar(avatarBytes: ByteArray): Result<Unit> {
        TODO("Not yet implemented")
    }
}