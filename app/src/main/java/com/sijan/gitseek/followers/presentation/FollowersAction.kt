package com.sijan.gitseek.followers.presentation

sealed interface FollowersAction{
    data class OnUserProfileClicked(val username: String): FollowersAction
    object OnBackClicked: FollowersAction
    object OnRetryClicked: FollowersAction
}