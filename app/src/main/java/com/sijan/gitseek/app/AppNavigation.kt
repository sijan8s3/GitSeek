package com.sijan.gitseek.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sijan.gitseek.search_user.presentation.SearchUserScreenRoot
import com.sijan.gitseek.search_user.presentation.SearchUserViewModel
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
        startDestination = NavigationRoute.GitSeekGraph,
    ) {

        navigation<NavigationRoute.GitSeekGraph>(
            startDestination = NavigationRoute.SearchUser,
        ) {
            composable<NavigationRoute.SearchUser> {
                val viewModel: SearchUserViewModel = koinViewModel()
                SearchUserScreenRoot(
                    viewModel = viewModel,
                    onFollowersClicked = { username ->
                    },
                    onFollowingClicked = { username ->
                    }
                )

            }

        }

    }

}
