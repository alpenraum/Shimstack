package com.alpenraum.shimstack.common

import android.os.Build
import androidx.biometric.BiometricManager

object CryptoConstants {
    val ALLOWED_BIOMETRIC_AUTHENTICATORS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
    } else {
        BiometricManager.Authenticators.BIOMETRIC_STRONG
    }
    const val ANDROID_KEYSTORE = "AndroidKeyStore"

    const val ENCRYPTED_PREFERENCES_NAME = "shimstack_secrets"
}
