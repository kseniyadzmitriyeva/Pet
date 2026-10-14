package com.furiqd.shared.ui.elements

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.furiqd.shared.R

@Composable
fun AvatarImage(
    avatarUrl: String?,
    modifier: Modifier = Modifier,
    @DrawableRes placeholderResId: Int = R.drawable.avatar_classic,
    contentDescription: String? = "User avatar"
) {
    val context = LocalContext.current
    val placeholderPainter = painterResource(id = placeholderResId)

    val imageRequest = remember(avatarUrl) {
        ImageRequest.Builder(context)
            .data(avatarUrl)
            .crossfade(true)
            .build()
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = placeholderPainter,
        error = placeholderPainter,
        fallback = placeholderPainter,
        contentScale = ContentScale.Crop
    )
}