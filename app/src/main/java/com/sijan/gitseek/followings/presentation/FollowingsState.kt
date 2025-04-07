package com.sijan.gitseek.followings.presentation

import com.sijan.gitseek.core.domain.utils.NetworkError
import com.sijan.gitseek.followers.domain.Follower
import com.sijan.gitseek.followings.domain.Following

data class FollowingsState(
    val isLoading: Boolean = false,
    val username: String? = null,
    val followings: List<Following> = emptyList(),
    val error: NetworkError? = null

)