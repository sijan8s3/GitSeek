package com.sijan.gitseek.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * A Composable function to observe events from a Flow and trigger a callback.
 *
 * It collects events from a Flow and calls the provided `onEvent` callback whenever an event is emitted.
 * The collection happens when the composable is in a lifecycle state of `STARTED` or higher.
 *
 * @param events The Flow of events to observe.
 * @param key1, key2 Optional keys to trigger recomposition when their values change.
 * @param onEvent Callback to handle each event emitted from the Flow.
 */

@Composable
fun <T> ObserveAsEvents(
    events: Flow<T>,
    key1: Any?= null,
    key2: Any?= null,
    onEvent: (T) -> Unit
) {
    val context= LocalContext.current
    val lifecycleOwner= LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                events.collect(onEvent)
            }
        }
    }
}