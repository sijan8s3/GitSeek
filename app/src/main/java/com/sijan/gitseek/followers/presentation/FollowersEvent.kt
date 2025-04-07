package com.sijan.gitseek.followers.presentation

import com.sijan.gitseek.core.domain.utils.NetworkError

sealed interface FollowersEvent {
    data class Error(val error: NetworkError) : FollowersEvent
}