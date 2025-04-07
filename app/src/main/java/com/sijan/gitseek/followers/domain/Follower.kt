package com.sijan.gitseek.followers.domain

data class Follower(
    val id: Long,
    val username: String,
    val avatarUrl: String?,
)