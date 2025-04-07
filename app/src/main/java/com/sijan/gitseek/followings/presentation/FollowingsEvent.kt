package com.sijan.gitseek.followings.presentation

import com.sijan.gitseek.core.domain.utils.NetworkError

sealed interface FollowingsEvent {
    data class Error(val error: NetworkError) : FollowingsEvent
}