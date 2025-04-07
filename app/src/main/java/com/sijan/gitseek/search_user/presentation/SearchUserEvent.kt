package com.sijan.gitseek.search_user.presentation


sealed interface SearchUserEvent {
    data class NavigateToUserProfile(val username: String): SearchUserEvent
}