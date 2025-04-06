package com.sijan.gitseek.core.domain.utils


enum class NetworkError: Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN,
    NO_INTERNET,
    UNAUTHORIZED
}
