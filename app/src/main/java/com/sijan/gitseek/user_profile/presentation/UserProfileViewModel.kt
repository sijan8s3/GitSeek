package com.sijan.gitseek.user_profile.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sijan.gitseek.core.domain.utils.onError
import com.sijan.gitseek.core.domain.utils.onSuccess
import com.sijan.gitseek.user_profile.domain.UserDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userDataSource: UserDataSource
) : ViewModel() {

    // StateFlow to hold the UI state
    private val _state = MutableStateFlow(UserProfileState())
    val state = _state.asStateFlow()

    // Channel for one-time events like error messages
    private val _events = Channel<UserProfileEvent>()
    val events = _events.receiveAsFlow()

    // Handle UI actions
    fun onAction(action: UserProfileAction) {
        when (action) {
            UserProfileAction.OnRetryClicked -> {
                getUserProfile()
            }

            UserProfileAction.OnRefreshPulled -> {
                refreshUserProfile()
            }

            else -> Unit
        }
    }

    private fun refreshUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            Log.d("TAG", "refreshUserProfile: refreshing")
            // Added a delay for demonstration purposes
            delay(1000) // 1 second delay
            getUserProfile(username = state.value.username)
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    fun setUsername(username: String) {
        _state.update {
            it.copy(username = username)
        }
        getUserProfile(username)
    }

    private fun getUserProfile(
        username: String? = null
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    profile = null,
                    error = null
                )
            }
            userDataSource.getUserProfile(username ?: state.value.username)
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
                    _events.send(UserProfileEvent.Error(error))
                }
        }
    }


}
