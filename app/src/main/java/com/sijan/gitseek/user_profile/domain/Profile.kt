package com.sijan.gitseek.user_profile.domain

data class Profile(
    val id: Long,
    val avatarUrl: String?,
    val username: String,
    val name: String?,
    val description: String?,
    val followers: Int,
    val following: Int,
)