package com.sijan.gitseek.followers.data.mappers

import com.sijan.gitseek.followers.data.networking.dto.FollowerDto
import com.sijan.gitseek.followers.domain.Follower


fun FollowerDto.toFollower(): Follower {
    return Follower(
        id = id,
        username = login,
        avatarUrl = avatar_url
    )
}
