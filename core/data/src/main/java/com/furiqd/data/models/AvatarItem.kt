package com.furiqd.data.models

sealed interface AvatarType {
    data class Resource(val resId: Int) : AvatarType
    data class CustomFile(val filePath: String) : AvatarType
    data class NetworkUrl(val url: String) : AvatarType
}

data class Avatar(
    val id: Int,
    val name: String,
    val type: AvatarType
)