package com.sijan.gitseek.search_user.presentation

import com.sijan.gitseek.core.domain.utils.NetworkError
import com.sijan.gitseek.search_user.domain.Profile

data class SearchUserState(
    val isLoading: Boolean = false,
    val username: String = "",
    val profile: Profile? = null,
    val error: NetworkError? = null
)