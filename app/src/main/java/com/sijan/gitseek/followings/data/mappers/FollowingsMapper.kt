package com.sijan.gitseek.followings.data.mappers

import com.sijan.gitseek.followings.data.networking.dto.FollowingDto
import com.sijan.gitseek.followings.domain.Following


fun FollowingDto.toFollowing(): Following {
    return Following(
        id = id,
        username = login,
        avatarUrl = avatar_url
    )
}
