package com.sijan.gitseek.search_user.presentation

import com.sijan.gitseek.core.domain.utils.NetworkError

sealed interface SearchUserEvent {
    data class Error(val error: NetworkError): SearchUserEvent
}