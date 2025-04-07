package com.sijan.gitseek.followings.data.networking

import com.sijan.gitseek.core.data.networking.constructUrl
import com.sijan.gitseek.core.data.networking.safeApiCall
import com.sijan.gitseek.core.domain.utils.NetworkError
import com.sijan.gitseek.core.domain.utils.Result
import com.sijan.gitseek.core.domain.utils.map
import com.sijan.gitseek.followers.data.mappers.toFollower
import com.sijan.gitseek.followers.data.networking.dto.FollowerDto
import com.sijan.gitseek.followers.domain.Follower
import com.sijan.gitseek.followers.domain.FollowersDataSource
import com.sijan.gitseek.followings.data.mappers.toFollowing
import com.sijan.gitseek.followings.data.networking.dto.FollowingDto
import com.sijan.gitseek.followings.domain.Following
import com.sijan.gitseek.followings.domain.FollowingsDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteFollowingsDataSource(
    private val httpClient: HttpClient
): FollowingsDataSource {
    override suspend fun getUserFollowings(username: String): Result<List<Following>, NetworkError> {
        return safeApiCall<List<FollowingDto>> {
            httpClient.get(urlString = constructUrl(url = "users/$username/following"))
        }.map { response ->
            response.map { it.toFollowing() }
        }
    }
}