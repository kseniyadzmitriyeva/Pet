package com.furiqd.personalisation.presentation.element

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun AvatarPickerSection(
    localAvatarUri: String?,
    isAvatarChosen: Boolean,
    onClick: () -> Unit
) {
    val avatarScale by animateFloatAsState(
        targetValue = if (isAvatarChosen) 1.05f else 1f,
        label = "AvatarScale"
    )

    Box(
        modifier = Modifier
            .size(120.dp)
            .scale(avatarScale)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 3.dp,
                color = if (isAvatarChosen) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        val avatarModel = remember(localAvatarUri) {
            if (!localAvatarUri.isNullOrBlank()) localAvatarUri else null
        }

        AsyncImage(
            model = avatarModel,
            contentDescription = "Selected Avatar Preview",
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
    }
}