package com.sijan.gitseek.di

import com.sijan.gitseek.core.data.networking.HttpClientFactory
import com.sijan.gitseek.followers.data.networking.RemoteFollowersDataSource
import com.sijan.gitseek.followers.domain.FollowersDataSource
import com.sijan.gitseek.followers.presentation.FollowersViewModel
import com.sijan.gitseek.followings.data.networking.RemoteFollowingsDataSource
import com.sijan.gitseek.followings.domain.FollowingsDataSource
import com.sijan.gitseek.followings.presentation.FollowingsViewModel
import com.sijan.gitseek.search_user.data.networking.RemoteUserDataSource
import com.sijan.gitseek.search_user.domain.UserDataSource
import com.sijan.gitseek.search_user.presentation.SearchUserViewModel
import com.sijan.gitseek.user_profile.presentation.UserProfileViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


// Koin module for dependency injection
// Define all app-specific dependencies here, such as:
// - ViewModels
// - Data Sources
// - Network Services

val appModule = module {

    single { HttpClientFactory.create(CIO.create()) }

    singleOf(::RemoteUserDataSource).bind<UserDataSource>()
    singleOf(::RemoteFollowersDataSource).bind<FollowersDataSource>()
    singleOf(::RemoteFollowingsDataSource).bind<FollowingsDataSource>()
    singleOf(::RemoteUserDataSource).bind<UserDataSource>()

    viewModelOf(::SearchUserViewModel)
    viewModelOf(::FollowersViewModel)
    viewModelOf(::FollowingsViewModel)
    viewModelOf(::UserProfileViewModel)


}