package com.furiqd.domain.model

sealed interface AvatarType {
    data class Resource(val resId: Int) : AvatarType
    data class CustomFile(val filePath: String) : AvatarType
    data class NetworkUrl(val url: String) : AvatarType
}