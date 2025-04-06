package com.sijan.gitseek.search_user.presentation

sealed interface SearchUserAction {
    data class OnUsernameChange(val username: String) : SearchUserAction
    data object OnSearchClicked : SearchUserAction
    data object OnRetryClicked : SearchUserAction
    data object onFollowersClicked : SearchUserAction
    data object onFollowingClicked : SearchUserAction


}