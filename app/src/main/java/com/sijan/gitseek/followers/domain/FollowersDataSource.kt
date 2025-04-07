package com.sijan.gitseek.followers.domain

import com.sijan.gitseek.core.domain.utils.NetworkError
import com.sijan.gitseek.core.domain.utils.Result

interface FollowersDataSource {
    suspend fun getUserFollowers(username: String): Result<List<Follower>, NetworkError>
}