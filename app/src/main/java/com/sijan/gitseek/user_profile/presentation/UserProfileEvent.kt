package com.sijan.gitseek.user_profile.presentation

import com.sijan.gitseek.core.domain.utils.NetworkError

sealed interface UserProfileEvent {
    data class Error(val error: NetworkError): UserProfileEvent
}