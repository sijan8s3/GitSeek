package com.sijan.gitseek.followers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sijan.gitseek.core.domain.utils.onError
import com.sijan.gitseek.core.domain.utils.onSuccess
import com.sijan.gitseek.followers.domain.FollowersDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FollowersViewModel(
    private val followersDataSource: FollowersDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(FollowersState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _state.value
    )


    fun onAction(action: FollowersAction) {
        when (action) {
            FollowersAction.OnRetryClicked -> {
                loadFollowers()
            }

            else -> Unit
        }
    }

    fun setUserName(username: String) {
        _state.update {
            it.copy(username = username)
        }
    }

    fun loadFollowers(
        username: String? = null
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true,
                    error = null,
                    followers = emptyList()
                    )
            }

            followersDataSource.getUserFollowers(
                username = username?: state.value.username?: ""
            ).onSuccess { followers ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        followers = followers,
                        error = null
                    )
                }

            }.onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = error,
                        followers = emptyList()
                    )
                }
            }

        }
    }


}