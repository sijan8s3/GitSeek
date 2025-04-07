package com.sijan.gitseek.app

import kotlinx.serialization.Serializable

sealed interface NavigationRoute{
    @Serializable
    data object GitSeekGraph: NavigationRoute

    @Serializable
    data object SearchUser: NavigationRoute

    @Serializable
    data class ProfileDetail(val userName: String): NavigationRoute

    @Serializable
    data class FollowersList(val userName: String): NavigationRoute

    @Serializable
    data class FollowingList(val userName: String): NavigationRoute

}