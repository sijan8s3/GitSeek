package com.sijan.gitseek.user_profile.presentation

sealed interface UserProfileAction {
    data object OnRetryClicked : UserProfileAction
    data object onFollowersClicked : UserProfileAction
    data object onFollowingClicked : UserProfileAction
    data object onBackClicked : UserProfileAction
    data object OnRefreshPulled : UserProfileAction
}