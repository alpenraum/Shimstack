package com.alpenraum.shimstack

import android.app.Application
import android.content.Context
import com.alpenraum.shimstack.datastore.ShimstackDatastore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ShimstackApplication : Application() {
    @Inject
    lateinit var datastore: ShimstackDatastore

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(
                object : Timber.Tree() {
                    override fun log(
                        priority: Int,
                        tag: String?,
                        message: String,
                        t: Throwable?
                    ) {
                        runBlocking {
                            if (datastore.allowAnalytics.first()) println(message)
                        }
                        // todo for the time this app actually sees the light of day -> integrate with [DeviceRepository#getDeviceId]
                    }
                }
            )
        }
    }

    companion object {
        var appContext: Context? = null
    }
}