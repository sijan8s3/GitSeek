package com.sijan.gitseek.app

import kotlinx.serialization.Serializable

sealed interface NavigationRoute{
    @Serializable
    data object GitSeekGraph: NavigationRoute

    @Serializable
    data object SearchUser: NavigationRoute

    @Serializable
    data class ProfileDetail(val userId: String): NavigationRoute

    @Serializable
    data class FollowersList(val userId: String): NavigationRoute

    @Serializable
    data class FollowingList(val userId: String): NavigationRoute

}