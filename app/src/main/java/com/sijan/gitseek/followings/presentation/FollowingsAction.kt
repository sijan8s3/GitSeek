package com.sijan.gitseek.followings.presentation

sealed interface FollowingsAction{
    data class OnUserProfileClicked(val username: String): FollowingsAction
    object OnBackClicked: FollowingsAction
    object OnRetryClicked: FollowingsAction
}