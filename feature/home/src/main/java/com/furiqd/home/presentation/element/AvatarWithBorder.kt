package com.furiqd.home.presentation.element

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.furiqd.shared.ui.elements.AvatarImage

@Composable
fun AvatarWithBorder(
    avatarUrl: String?,
    isDone: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isDone) Color(0xFF4CAF50) else MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .size(74.dp)
            .border(4.dp, borderColor, CircleShape)
            .padding(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            AvatarImage(
                avatarUrl = avatarUrl,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}