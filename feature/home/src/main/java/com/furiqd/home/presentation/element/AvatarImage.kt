package com.furiqd.home.presentation.element

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.furiqd.designsystem.R

@Composable
fun AvatarImage(
    avatarUrl: String?,
    modifier: Modifier = Modifier,
    @DrawableRes placeholderResId: Int = R.drawable.avatar_girl_svgrepo_com__11_
) {
    if (avatarUrl.isNullOrBlank()) {
        Image(
            painter = painterResource(placeholderResId),
            contentDescription = "Basic avatar",
            modifier = modifier
        )
        return
    } else {
        SubcomposeAsyncImage(
            model = avatarUrl,
            contentDescription = "User avatar",
            modifier = modifier,
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
            },
            error = {
                Image(
                    painter = painterResource(id = placeholderResId),
                    contentDescription = "Fallback avatar",
                    contentScale = ContentScale.Crop
                )
            }
        )
    }

}
