package com.furiqd.designsystem.config

import com.furiqd.designsystem.R

data class AvatarResource(
    val id: Int,
    val drawableResId: Int
)

object AvatarConfig {
    val avatars = listOf(
        AvatarResource(id = 1, drawableResId = R.drawable.avatar_boy_svgrepo_com__1_),
        AvatarResource(id = 2, drawableResId = R.drawable.avatar_boy_svgrepo_com__3_),
        AvatarResource(id = 3, drawableResId = R.drawable.avatar_boy_svgrepo_com__4_),
        AvatarResource(id = 4, drawableResId = R.drawable.avatar_girl_svgrepo_com__9_),
        AvatarResource(id = 5, drawableResId = R.drawable.avatar_girl_svgrepo_com__12_),
        AvatarResource(id = 6, drawableResId = R.drawable.avatar_girl_svgrepo_com__10_)
    )

    fun getDrawableById(id: Int?): Int {
        return avatars.find { it.id == id }?.drawableResId
            ?: R.drawable.avatar_girl_svgrepo_com__11_
    }
}