@file:OptIn(ExperimentalMaterial3Api::class)

package com.sijan.gitseek.user_profile.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.sijan.gitseek.core.domain.utils.NetworkError
import com.sijan.gitseek.core.presentation.components.HeaderBackIcon
import com.sijan.gitseek.core.presentation.components.ProgressIndicator
import com.sijan.gitseek.core.presentation.utils.toString
import com.sijan.gitseek.search_user.domain.Profile
import com.sijan.gitseek.user_profile.presentation.components.shimmerEffect

@Composable
fun UserProfileScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel,
    onBackClicked: () -> Unit,
    onFollowersClicked: (String) -> Unit,
    onFollowingClicked: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    UserProfileScreen(
        modifier = modifier.systemBarsPadding(),
        state = state,
        onAction = { action ->
            when (action) {
                UserProfileAction.onFollowersClicked -> {
                    state.profile?.let {
                        onFollowersClicked(it.username)
                    }
                }

                UserProfileAction.onFollowingClicked -> {
                    state.profile?.let {
                        onFollowingClicked(it.username)
                    }
                }

                UserProfileAction.onBackClicked -> {
                    onBackClicked()
                }

                else -> Unit
            }
            viewModel.onAction(action)

        }
    )

}


@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    state: UserProfileState,
    onAction: (UserProfileAction) -> Unit
) {
    val context = LocalContext.current

    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onAction(UserProfileAction.OnRefreshPulled) },
        state = pullToRefreshState,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                HeaderBackIcon(
                    text = "User Profile",
                    onClickBack = {
                        onAction(UserProfileAction.onBackClicked)
                    }
                )
            }

            if (state.isLoading) {
                items(1) { // Show 3 shimmer items as placeholders
                    ShimmerProfileItem()
                }
            } else if (state.error != null) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = state.error.toString(context = context)
                        )
                        Button(onClick = {
                            onAction(UserProfileAction.OnRetryClicked)
                        }) {
                            Text(text = "Retry")
                        }
                    }
                }
            } else {
                state.profile?.let {
                    item {
                        ProfileSection(
                            profile = it,
                            onFollowersClick = { onAction(UserProfileAction.onFollowersClicked) },
                            onFollowingClick = { onAction(UserProfileAction.onFollowingClicked) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ProfileSection(
    profile: Profile,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = profile.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = profile.name ?: profile.username,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "@${profile.username}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        profile.description?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${profile.followers} followers",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onFollowersClick() }
            )
            Text(
                text = "${profile.following} following",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onFollowingClick() }
            )
        }
    }
}


@Composable
fun ShimmerProfileItem(modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .shimmerEffect()
            )
        }
    }
}


