package com.sijan.gitseek.search_user.presentation

import com.sijan.gitseek.user_profile.domain.Profile


data class SearchUserState(
    val isLoading: Boolean = false,
    val username: String = "",
    val profile: Profile? = null,
    val error: String? = null
)