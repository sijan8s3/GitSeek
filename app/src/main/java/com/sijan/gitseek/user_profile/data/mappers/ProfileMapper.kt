package com.sijan.gitseek.user_profile.data.mappers

import com.sijan.gitseek.search_user.data.networking.dto.ProfileDto
import com.sijan.gitseek.search_user.domain.Profile

fun ProfileDto.toProfile(): Profile {
    return Profile(
        id = id,
        avatarUrl = avatar_url,
        username = login,
        name = name,
        description = bio,
        followers = followers?: 0,
        following = following?: 0
    )
}