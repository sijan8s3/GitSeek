package com.sijan.gitseek.app

import android.app.Application
import com.sijan.gitseek.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Application class for the GitSeek App
 *
 * This class is responsible for initializing global app configurations,
 * including starting the Koin Dependency Injection framework.
 */
class GitSeekApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger() // Log Koin-related events
            androidContext(this@GitSeekApp) // Provide the application context to Koin
            modules(appModule) // Load the app's dependency module
        }
    }
}