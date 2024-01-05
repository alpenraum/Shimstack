package com.alpenraum.shimstack.usecases.biometrics

import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class TriggerBiometricsPromptUseCase {
    operator fun invoke(
        activity: FragmentActivity,
        authenticationCallback: AuthenticationCallback
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt =
            BiometricPrompt(
                activity,
                executor,
                authenticationCallback
            )

        val promptInfo =
            BiometricPrompt.PromptInfo.Builder().setTitle(
                "Biometric login for my app"
            )
                .setSubtitle("Log in using your biometric credential").setNegativeButtonText(
                    "Use account password"
                ).build()

        biometricPrompt.authenticate(promptInfo)
    }
}