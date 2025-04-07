package com.sijan.gitseek.search_user.presentation

sealed interface SearchUserAction {
    data class OnUsernameChange(val username: String) : SearchUserAction
    data object OnSearchClicked : SearchUserAction
}