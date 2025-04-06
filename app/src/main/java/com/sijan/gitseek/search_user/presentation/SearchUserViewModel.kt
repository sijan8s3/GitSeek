package com.sijan.gitseek.search_user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sijan.gitseek.core.domain.utils.onError
import com.sijan.gitseek.core.domain.utils.onSuccess
import com.sijan.gitseek.search_user.domain.UserDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchUserViewModel(
    private val userDataSource: UserDataSource
) : ViewModel() {

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
                _state.value = _state.value.copy(
                    username = action.username
                )
            }

            SearchUserAction.OnRetryClicked -> {
                getUserProfile()
            }

            SearchUserAction.OnSearchClicked -> getUserProfile()
           else -> Unit
        }

    }

    private fun getUserProfile() {
        if (state.value.username.isBlank()) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true,
                    profile = null,
                    error = null
                )
            }

            userDataSource.getUserProfile(state.value.username)
                .onSuccess { user ->
                    _state.update {
                        it.copy(
                            profile = user,
                            isLoading = false,
                            error = null
                        )
                    }
                }.onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error,
                            profile = null
                        )
                    }
                    _events.send(SearchUserEvent.Error(error))
                }
        }
    }


}
