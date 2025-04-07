package com.sijan.gitseek.followers.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sijan.gitseek.core.domain.utils.Result
import com.sijan.gitseek.followers.domain.Follower
import com.sijan.gitseek.followers.domain.FollowersDataSource
import com.sijan.gitseek.core.domain.utils.NetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class FollowersViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var followersDataSource: FollowersDataSource

    private lateinit var viewModel: FollowersViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val followers = listOf(
        Follower(id = 1, username = "follower1", avatarUrl = "https://example.com/avatar1.png"),
        Follower(id = 2, username = "follower2", avatarUrl = "https://example.com/avatar2.png")
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = FollowersViewModel(followersDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadFollowers should update state with followers on success`() = runTest {
        // Arrange
        `when`(followersDataSource.getUserFollowers("testUser")).thenReturn(Result.Success(followers))

        // Act
        val stateUpdates = mutableListOf<FollowersState>()
        val job = launch {
            viewModel.state.toList(stateUpdates)
        }

        viewModel.setUserName("testUser")
        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        // Assert
        assertEquals(
            FollowersState(isLoading = false, followers = followers, username = "testUser"),
            stateUpdates.last()
        )
    }

    @Test
    fun `loadFollowers should update state with error on failure`() = runTest {
        // Arrange
        val error = NetworkError.NO_INTERNET
        `when`(followersDataSource.getUserFollowers("testUser")).thenReturn(Result.Error(error))

        // Act
        val stateUpdates = mutableListOf<FollowersState>()
        val job = launch {
            viewModel.state.toList(stateUpdates)
        }

        viewModel.setUserName("testUser")
        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        // Assert
        assertEquals(
            FollowersState(isLoading = false, error = error, username = "testUser"),
            stateUpdates.last()
        )
    }
}