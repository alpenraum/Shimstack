package com.alpenraum.shimstack.common

import android.hardware.biometrics.BiometricManager
import android.os.Build

object CryptoConstants {
    val ALLOWED_BIOMETRIC_AUTHENTICATORS =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        } else {
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        }
}