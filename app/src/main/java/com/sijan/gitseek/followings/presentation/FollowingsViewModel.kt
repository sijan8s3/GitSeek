package com.sijan.gitseek.followings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sijan.gitseek.core.domain.utils.onError
import com.sijan.gitseek.core.domain.utils.onSuccess
import com.sijan.gitseek.followers.domain.FollowersDataSource
import com.sijan.gitseek.followings.domain.FollowingsDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FollowingsViewModel(
    private val followingsDataSource: FollowingsDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(FollowingsState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _state.value
    )


    fun onAction(action: FollowingsAction) {
        when (action) {
            FollowingsAction.OnRetryClicked -> {
                loadFollowings()
            }

            FollowingsAction.OnRefresh -> {
                refreshFollowings()
            }

            else -> Unit
        }
    }

    private fun refreshFollowings() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            loadFollowings()
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    fun setUserName(username: String) {
        _state.update {
            it.copy(username = username)
        }
        loadFollowings(username)

    }

    fun loadFollowings(
        username: String? = null
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    followings = emptyList()
                )
            }

            followingsDataSource.getUserFollowings(
                username = username ?: state.value.username ?: ""
            ).onSuccess { followings ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        followings = followings,
                        error = null
                    )
                }

            }.onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = error,
                        followings = emptyList()
                    )
                }
            }

        }
    }


}