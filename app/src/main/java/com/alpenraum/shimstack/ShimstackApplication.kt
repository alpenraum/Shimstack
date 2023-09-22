package com.alpenraum.shimstack

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShimstackApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
    }

    companion object {

        lateinit var appContext: Context
    }
}
