package com.sijan.gitseek.user_profile.presentation

import com.sijan.gitseek.core.domain.utils.NetworkError
import com.sijan.gitseek.user_profile.domain.Profile

data class UserProfileState(
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val username: String = "",
    val profile: Profile? = null,
    val error: NetworkError? = null
)