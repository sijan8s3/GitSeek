package com.sijan.gitseek.followers.data.networking

import com.sijan.gitseek.core.data.networking.constructUrl
import com.sijan.gitseek.core.data.networking.safeApiCall
import com.sijan.gitseek.core.domain.utils.NetworkError
import com.sijan.gitseek.core.domain.utils.Result
import com.sijan.gitseek.core.domain.utils.map
import com.sijan.gitseek.followers.data.mappers.toFollower
import com.sijan.gitseek.followers.data.networking.dto.FollowerDto
import com.sijan.gitseek.followers.domain.Follower
import com.sijan.gitseek.followers.domain.FollowersDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteFollowersDataSource(
    private val httpClient: HttpClient
): FollowersDataSource {
    override suspend fun getUserFollowers(username: String): Result<List<Follower>, NetworkError> {
        return safeApiCall<List<FollowerDto>> {
            httpClient.get(urlString = constructUrl(url = "users/$username/followers"))
        }.map { response ->
            response.map { it.toFollower() }
        }
    }
}