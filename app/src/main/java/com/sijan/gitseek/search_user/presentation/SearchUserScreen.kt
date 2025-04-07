package com.sijan.gitseek.search_user.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.sijan.gitseek.core.presentation.components.ProgressIndicator
import com.sijan.gitseek.core.presentation.utils.toString
import com.sijan.gitseek.search_user.domain.Profile
import com.sijan.gitseek.search_user.presentation.components.UserSearchBar

@Composable
fun SearchUserScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SearchUserViewModel,
    onFollowersClicked: (String) -> Unit,
    onFollowingClicked: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SearchUserScreen(
        modifier = modifier.systemBarsPadding(),
        state = state,
        onAction = { action ->
            when (action) {
                SearchUserAction.onFollowersClicked -> {
                    state.profile?.let {
                        onFollowersClicked(it.username)
                    }
                }

                SearchUserAction.onFollowingClicked -> {
                    state.profile?.let {
                        onFollowingClicked(it.username)
                    }
                }

                else -> Unit
            }
            viewModel.onAction(action)

        }
    )

}


@Composable
fun SearchUserScreen(
    modifier: Modifier = Modifier,
    state: SearchUserState,
    onAction: (SearchUserAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .clickable(
                onClick = {
                    localFocusManager.clearFocus()
                },
                indication = null,
                interactionSource = null
            )
    ) {

        UserSearchBar(
            searchQuery = state.username,
            onSearchQueryChange = {
                onAction(SearchUserAction.OnUsernameChange(it))
            },
            onImeSearch = {
                if (state.username.isNotBlank())
                    onAction(SearchUserAction.OnSearchClicked)

                onAction(SearchUserAction.OnSearchClicked)
                keyboardController?.hide()
                localFocusManager.clearFocus()
            },
            modifier = Modifier
                .widthIn(max = 700.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            ProgressIndicator()
        }
        if (state.error != null && !state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = state.error.toString(context = context))
                Button(onClick = {
                    onAction(SearchUserAction.OnRetryClicked)
                }) {
                    Text(text = "Retry")
                }
            }
        }

        state.profile?.let {
            ProfileCard(
                profile = state.profile,
                onFollowersClick = { onAction(SearchUserAction.onFollowersClicked) },
                onFollowingClick = { onAction(SearchUserAction.onFollowingClicked) }
            )

        }
    }
}


@Composable
fun ProfileCard(
    profile: Profile,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = profile.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = profile.name ?: profile.username,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "@${profile.username}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            profile.description?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onFollowersClick) {
                    Text("${profile.followers} followers")
                }
                TextButton(onClick = onFollowingClick) {
                    Text("${profile.following} following")
                }
            }
        }
    }
}

