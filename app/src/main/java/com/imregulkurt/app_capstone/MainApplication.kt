package com.imregulkurt.app_capstone

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    // Singleton instance
    companion object {
        lateinit var appContext: Context
    }
}
