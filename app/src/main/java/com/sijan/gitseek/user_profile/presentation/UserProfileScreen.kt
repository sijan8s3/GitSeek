@file:OptIn(ExperimentalMaterial3Api::class)

package com.sijan.gitseek.user_profile.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.sijan.gitseek.core.presentation.components.HeaderBackIcon
import com.sijan.gitseek.core.presentation.components.ProgressIndicator
import com.sijan.gitseek.core.presentation.utils.toString
import com.sijan.gitseek.search_user.domain.Profile
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

    // Remember the pull-to-refresh state
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onAction(UserProfileAction.OnRefreshPulled) },
        state = pullToRefreshState,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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
                item {
                    ProgressIndicator()
                }
            }

            if (state.error != null && !state.isLoading) {
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
            }

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


