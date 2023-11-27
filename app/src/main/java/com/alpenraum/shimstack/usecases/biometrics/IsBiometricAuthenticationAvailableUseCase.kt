package com.alpenraum.shimstack.usecases.biometrics

import android.app.Activity
import androidx.biometric.BiometricManager
import com.alpenraum.shimstack.common.CryptoConstants.ALLOWED_BIOMETRIC_AUTHENTICATORS

class IsBiometricAuthenticationAvailableUseCase {

    sealed class Result {
        object Success : Result()
        object Failure : Result()
        object NoneEnrolled : Result()

        fun isSuccess() = this is Success
        fun isFailure() = this is Failure
    }

    operator fun invoke(activity: Activity): Result {
        val biometricManager = BiometricManager.from(activity)
        return when (biometricManager.canAuthenticate(ALLOWED_BIOMETRIC_AUTHENTICATORS)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Result.Success

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Result.Failure

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Result.Failure

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Result.NoneEnrolled
            }

            else -> {
                Result.Failure
            }
        }
    }
}
