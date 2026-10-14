package com.furiqd.home.presentation.element

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.furiqd.shared.ui.elements.AvatarImage

@Composable
fun AvatarWithEditButton(
    avatarUrl: String?,
    modifier: Modifier = Modifier,
    onAddAvatarClick: () -> Unit
) {
    Box(modifier = modifier.size(120.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Color.LightGray)
                .border(width = 2.dp, color = Color.White, shape = CircleShape)
        ) {
            AvatarImage(
                avatarUrl = avatarUrl,
                modifier = Modifier.fillMaxSize()
            )

        }
        IconButton(
            onClick = onAddAvatarClick,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.BottomEnd)
                .background(Color.White, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Add avatar",
                tint = Color.Black
            )
        }
    }
}