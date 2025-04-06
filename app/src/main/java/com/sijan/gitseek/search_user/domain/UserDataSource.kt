package com.sijan.gitseek.search_user.domain

import com.sijan.gitseek.core.domain.utils.NetworkError
import com.sijan.gitseek.core.domain.utils.Result

interface UserDataSource {
    suspend fun getUserProfile(username: String): Result<Profile, NetworkError>
}