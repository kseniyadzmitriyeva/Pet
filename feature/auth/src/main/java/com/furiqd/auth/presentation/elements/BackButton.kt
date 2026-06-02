package com.furiqd.auth.presentation.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.furiqd.auth.presentation.screens.signin.SignInEvent
import com.furiqd.designsystem.R.drawable

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.padding(top = 16.dp)
    ) {
        Icon(
            painter = painterResource(id = drawable.arrow_narrow_left_icon_151607),
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}