package com.sijan.gitseek.search_user.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sijan.gitseek.core.presentation.utils.ObserveAsEvents
import com.sijan.gitseek.search_user.presentation.components.UserSearchBar

@Composable
fun SearchUserScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SearchUserViewModel,
    navigateToUserProfile: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvents(events = viewModel.events) { event ->
        when (event) {
            is SearchUserEvent.NavigateToUserProfile -> {
                keyboardController?.hide()
                navigateToUserProfile(event.username)

            }
        }
    }
    SearchUserScreen(
        modifier = modifier.systemBarsPadding(),
        state = state,
        onAction = { action ->
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
    val localFocusManager = LocalFocusManager.current

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
            error = state.error,
            searchQuery = state.username,
            onSearchQueryChange = {
                onAction(SearchUserAction.OnUsernameChange(it))
            },
            onImeSearch = {
                onAction(SearchUserAction.OnSearchClicked)
            },
            modifier = Modifier
                .widthIn(max = 700.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))



    }
}


