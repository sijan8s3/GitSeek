package com.sijan.gitseek.followings.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sijan.gitseek.core.presentation.components.HeaderBackIcon
import com.sijan.gitseek.core.presentation.components.ProgressIndicator
import com.sijan.gitseek.core.presentation.utils.toString
import com.sijan.gitseek.followers.presentation.components.FollowerItem
import com.sijan.gitseek.followings.presentation.components.FollowingItem
import com.sijan.gitseek.followings.presentation.components.ShimmerFollowingItem

@Composable
fun FollowingsListScreenRoot(
    viewModel: FollowingsViewModel,
    onUserProfileClicked: (String) -> Unit,
    onBackClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    FollowingsListScreen(
        modifier = Modifier.statusBarsPadding(),
        state = state,
        onAction = { action ->
            when (action) {
                FollowingsAction.OnBackClicked -> onBackClicked()
                is FollowingsAction.OnUserProfileClicked -> onUserProfileClicked(action.username)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FollowingsListScreen(
    modifier: Modifier,
    state: FollowingsState,
    onAction: (FollowingsAction) -> Unit,
) {
    val context = LocalContext.current

    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onAction(FollowingsAction.OnRefresh) },
        state = pullToRefreshState,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                HeaderBackIcon(
                    text = "Following",
                    onClickBack = {
                        onAction(FollowingsAction.OnBackClicked)
                    }
                )
            }

            if (state.isLoading) {
                items(10) { // Show 10 shimmer items as placeholders
                    ShimmerFollowingItem()
                }
            } else {
                items(
                    items = state.followings,
                    key = { user -> user.id }
                ) { user ->
                    FollowingItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        user = user,
                        onClick = {
                            onAction(FollowingsAction.OnUserProfileClicked(user.username))
                        }
                    )
                }
            }
        }

        if (state.error != null) {
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
                    onAction(FollowingsAction.OnRetryClicked)
                }) {
                    Text(text = "Retry")
                }
            }
        }

        if (state.followings.isEmpty() && !state.isLoading && state.error == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No following found.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

