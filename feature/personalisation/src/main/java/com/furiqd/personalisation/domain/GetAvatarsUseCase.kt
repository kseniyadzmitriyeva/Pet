package com.furiqd.personalisation.domain

import com.furiqd.domain.model.Avatar
import com.furiqd.domain.repository.AvatarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvatarsUseCase@Inject constructor(
    private val avatarRepository: AvatarRepository
) {
    operator fun invoke(): Flow<List<Avatar>>{
        return avatarRepository.getAvailableAvatars()
    }
}