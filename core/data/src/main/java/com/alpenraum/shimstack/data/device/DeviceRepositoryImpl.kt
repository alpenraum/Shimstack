package com.alpenraum.shimstack.data.device

import android.content.Context
import android.provider.Settings
import javax.inject.Inject

class DeviceRepositoryImpl
    @Inject
    constructor(
        private val context: Context
    ) : DeviceRepository {
        // TODO: maybe move to firebase-id to have compability with crashlytics
        override fun getDeviceId(): String =
            Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
            )
    }