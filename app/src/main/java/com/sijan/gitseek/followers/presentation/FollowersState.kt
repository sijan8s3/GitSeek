package com.sijan.gitseek.followers.presentation

import com.sijan.gitseek.core.domain.utils.NetworkError
import com.sijan.gitseek.followers.domain.Follower

data class FollowersState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val username: String? = null,
    val followers: List<Follower> = emptyList(),
    val error: NetworkError? = null

)