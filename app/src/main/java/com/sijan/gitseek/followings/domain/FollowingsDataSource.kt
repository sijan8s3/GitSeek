package com.sijan.gitseek.followings.domain

import com.sijan.gitseek.core.domain.utils.NetworkError
import com.sijan.gitseek.core.domain.utils.Result

interface FollowingsDataSource {
    suspend fun getUserFollowings(username: String): Result<List<Following>, NetworkError>
}