package com.sijan.gitseek.user_profile.presentation


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sijan.gitseek.core.domain.utils.Result
import com.sijan.gitseek.user_profile.domain.UserDataSource
import com.sijan.gitseek.user_profile.domain.Profile
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
class UserProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userDataSource: UserDataSource

    private lateinit var viewModel: UserProfileViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val profile = Profile(
        id = 1,
        username = "testUser",
        name = "Test User",
        avatarUrl = "https://example.com/avatar.png",
        description = "Test Description",
        followers = 10,
        following = 20
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = UserProfileViewModel(userDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setUsername should update state with profile on success`() = runTest {
        // Arrange
        `when`(userDataSource.getUserProfile("testUser")).thenReturn(Result.Success(profile))

        // Act
        viewModel.setUsername("testUser")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(
            UserProfileState(isLoading = false, profile = profile, username = "testUser"),
            viewModel.state.value
        )
    }

    @Test
    fun `setUsername should update state with error on failure`() = runTest {
        // Arrange
        val error = NetworkError.NO_INTERNET
        `when`(userDataSource.getUserProfile("testUser")).thenReturn(Result.Error(error))

        // Act
        viewModel.setUsername("testUser")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(
            UserProfileState(isLoading = false, error = error, username = "testUser"),
            viewModel.state.value
        )
    }

    @Test
    fun `onAction OnRetryClicked should retry loading profile`() = runTest {
        // Arrange
        `when`(userDataSource.getUserProfile("testUser")).thenReturn(Result.Success(profile))

        // Act
        viewModel.setUsername("testUser")
        viewModel.onAction(UserProfileAction.OnRetryClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(
            UserProfileState(isLoading = false, profile = profile, username = "testUser"),
            viewModel.state.value
        )
    }

}
