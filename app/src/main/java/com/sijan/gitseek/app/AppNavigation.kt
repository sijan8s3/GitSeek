package com.sijan.gitseek.app

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.sijan.gitseek.app.NavigationRoute.FollowersList
import com.sijan.gitseek.app.NavigationRoute.FollowingList
import com.sijan.gitseek.app.NavigationRoute.GitSeekGraph
import com.sijan.gitseek.app.NavigationRoute.SearchUser
import com.sijan.gitseek.app.NavigationRoute.UserProfile
import com.sijan.gitseek.followers.presentation.FollowersListScreenRoot
import com.sijan.gitseek.followers.presentation.FollowersViewModel
import com.sijan.gitseek.followings.presentation.FollowingsListScreenRoot
import com.sijan.gitseek.followings.presentation.FollowingsViewModel
import com.sijan.gitseek.search_user.presentation.SearchUserScreenRoot
import com.sijan.gitseek.search_user.presentation.SearchUserViewModel
import com.sijan.gitseek.user_profile.presentation.UserProfileScreenRoot
import com.sijan.gitseek.user_profile.presentation.UserProfileViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Composable function to set up the app's navigation graph.
 *
 * Defines the navigation structure and routes for the app's screens
 */

@Composable
fun AppNavigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = GitSeekGraph,
    ) {

        navigation<GitSeekGraph>(
            startDestination = SearchUser,
        ) {
            composable<SearchUser> {
                val viewModel: SearchUserViewModel = koinViewModel()
                SearchUserScreenRoot(
                    viewModel = viewModel,
                    navigateToUserProfile = { username ->
                        navController.navigate(UserProfile(username))
                    }
                )
            }

            composable<FollowersList> { backStackEntry ->
                val profile = backStackEntry.toRoute<FollowersList>()
                val username = profile.userName
                Log.d("TAG", "AppNavigation: username: $username ")
                val viewModel: FollowersViewModel = koinViewModel()
                LaunchedEffect(key1 = Unit){
                    viewModel.setUserName(username)
                }
                FollowersListScreenRoot(
                    viewModel = viewModel,
                    onUserProfileClicked = { username ->
                        navController.navigate(UserProfile(username))
                    },
                    onBackClicked = {
                        navController.navigateUp()
                    }
                )
            }


            composable<FollowingList> { backStackEntry ->
                val profile = backStackEntry.toRoute<FollowingList>()
                val username = profile.userName
                Log.d("TAG", "AppNavigation: username: $username ")
                val viewModel: FollowingsViewModel = koinViewModel()
                LaunchedEffect(key1 = Unit){
                    viewModel.setUserName(username)
                }
                FollowingsListScreenRoot(
                    viewModel = viewModel,
                    onUserProfileClicked = { username ->
                        navController.navigate(UserProfile(username))
                    },
                    onBackClicked = {
                        navController.navigateUp()
                    }
                )
            }


            composable<UserProfile> { backStackEntry ->
                val profile = backStackEntry.toRoute<UserProfile>()
                val username = profile.userName
                val viewModel: UserProfileViewModel = koinViewModel()
                LaunchedEffect(key1 = Unit){
                    viewModel.setUsername(username)
                }
                UserProfileScreenRoot(
                    viewModel = viewModel,
                    onBackClicked = {
                        navController.navigateUp()
                    },
                    onFollowersClicked = { username ->
                        navController.navigate(FollowersList(username))
                    },
                    onFollowingClicked = { username ->
                        navController.navigate(FollowingList(username))
                    }
                )
            }

        }

    }

}

