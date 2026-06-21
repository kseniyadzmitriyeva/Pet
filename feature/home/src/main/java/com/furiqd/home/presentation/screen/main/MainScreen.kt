package com.furiqd.home.presentation.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.furiqd.designsystem.theme.ItsAliveTheme // Подключаем вашу тему
import com.furiqd.home.presentation.element.ActiveRoomCard
import com.furiqd.home.presentation.element.AvatarWithBorder
import com.furiqd.home.presentation.element.CreateRoomPlaceholderCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: MainState,
    onCameraClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCreateRoomClick: () -> Unit,
    onInciteFriendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { state.rooms.size })

    Column(modifier){
        Row(
            modifier = modifier
                .height(60.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onProfileClick) {
                Icon(
                    modifier = modifier.fillMaxSize(),
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile")
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            pageSpacing = 16.dp
        ) { page ->
            when (val room = state.rooms[page]) {
                is RoomUiModel.Active -> {
                    ActiveRoomCard(
                        room = room,
                        onCameraClick = onCameraClick,
                        modifier = modifier,
                        onInciteFriendClick = onInciteFriendClick
                    )
                }
                is RoomUiModel.Placeholder -> {
                    CreateRoomPlaceholderCard(
                        onCreateRoomClick = onCreateRoomClick,
                        modifier = modifier
                    )
                }
            }
        }
    }
}
@Composable
fun MainRoute(
    onNavigateToProfile: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    // В будущем тут будет: val state by viewModel.uiState.collectAsStateWithLifecycle()
    // А пока передаем временный фейковый стейт для компиляции:
    val fakeState = MainState()

    MainScreen(
        state = fakeState,
        onCameraClick = { /* TODO: Открыть камеру */ },
        onProfileClick = onNavigateToProfile,
        onCreateRoomClick = { /* TODO: Создать комнату */ },
        onInciteFriendClick = { /* TODO: Пригласить друга */ }
    )
}