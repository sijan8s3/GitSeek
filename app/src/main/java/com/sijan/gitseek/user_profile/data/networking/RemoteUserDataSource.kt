package com.sijan.gitseek.user_profile.data.networking

import com.sijan.gitseek.core.data.networking.constructUrl
import com.sijan.gitseek.core.data.networking.safeApiCall
import com.sijan.gitseek.core.domain.utils.NetworkError
import com.sijan.gitseek.core.domain.utils.Result
import com.sijan.gitseek.core.domain.utils.map
import com.sijan.gitseek.user_profile.data.mappers.toProfile
import com.sijan.gitseek.user_profile.data.networking.dto.ProfileDto
import com.sijan.gitseek.user_profile.domain.Profile
import com.sijan.gitseek.user_profile.domain.UserDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteUserDataSource(
    private val httpClient: HttpClient
) : UserDataSource {
    override suspend fun getUserProfile(username: String): Result<Profile, NetworkError> {
        return safeApiCall<ProfileDto> {
            httpClient.get(urlString = constructUrl(url = "/users/$username"))
        }.map { response ->
            response.toProfile()
        }
    }
}