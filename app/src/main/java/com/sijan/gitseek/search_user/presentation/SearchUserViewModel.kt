package com.sijan.gitseek.search_user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchUserViewModel() : ViewModel() {

    // StateFlow to hold the UI state
    private val _state = MutableStateFlow(SearchUserState())
    val state = _state
        .onStart {}.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value
        )

    // Channel for one-time events like error messages
    private val _events = Channel<SearchUserEvent>()
    val events = _events.receiveAsFlow()

    // Handle UI actions
    fun onAction(action: SearchUserAction) {
        when (action) {
            is SearchUserAction.OnUsernameChange -> {
                val error = validateUsername(action.username)
                _state.value = _state.value.copy(
                    username = action.username,
                    error = error
                )
            }
            SearchUserAction.OnSearchClicked -> {
                val username = state.value.username.trim()
                val error = validateUsername(username)

                if (error != null) {
                    _state.value = _state.value.copy(error = error)
                } else {
                    viewModelScope.launch {
                        _events.send(SearchUserEvent.NavigateToUserProfile(username))
                    }
                }
            }
        }
    }

    private fun validateUsername(username: String): String? {
        val trimmedUsername = username.trim()
        val usernamePattern = "^[a-zA-Z0-9-]+$".toRegex()

        return when {
            trimmedUsername.isBlank() -> "Username cannot be empty"
            !usernamePattern.matches(trimmedUsername) -> "Username can only contain alphanumeric characters and dashes"
            else -> null
        }
    }

}
