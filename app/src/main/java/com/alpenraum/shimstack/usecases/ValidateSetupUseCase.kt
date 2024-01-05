package com.alpenraum.shimstack.usecases

import com.alpenraum.shimstack.ui.features.newBike.SetupInputData

class ValidateSetupUseCase {
    operator fun invoke(setupInputData: SetupInputData): Result {
        val results =
            buildList {
                add(
                    setupInputData.frontTirePressure?.toDoubleOrNull()
                        ?.let { validateTirePressure(it) }
                        ?: SubResult.FAILURE
                )
                add(
                    setupInputData.rearTirePressure?.toDoubleOrNull()
                        ?.let { validateTirePressure(it) }
                        ?: SubResult.FAILURE
                )
                add(
                    setupInputData.frontSuspensionPressure?.toDoubleOrNull()
                        ?.let { validateSuspensionPressure(it, true) } ?: SubResult.FAILURE
                )
                add(
                    setupInputData.rearSuspensionPressure?.toDoubleOrNull()
                        ?.let { validateSuspensionPressure(it, false) } ?: SubResult.FAILURE
                )
                add(
                    validateSuspensionSetup(
                        setupInputData.frontSuspensionLSC?.toIntOrNull(),
                        setupInputData.frontSuspensionHSC?.toIntOrNull(),
                        setupInputData.frontSuspensionLSR?.toIntOrNull(),
                        setupInputData.frontSuspensionHSR?.toIntOrNull()
                    )
                )
                add(
                    validateSuspensionSetup(
                        setupInputData.rearSuspensionLSC?.toIntOrNull(),
                        setupInputData.rearSuspensionHSC?.toIntOrNull(),
                        setupInputData.rearSuspensionLSR?.toIntOrNull(),
                        setupInputData.rearSuspensionHSR?.toIntOrNull()
                    )
                )
            }

        return if (results.any { it == SubResult.FAILURE }) {
            SetupFailure
        } else {
            if (results.any { it == SubResult.OUTLIER }) SetupOutlier else Result.Success()
        }
    }

    private fun validateTirePressure(pressure: Double): SubResult {
        if (pressure <= 0.0) return SubResult.FAILURE
        return if (pressure >= 3.0) SubResult.OUTLIER else SubResult.SUCCESS
    }

    private fun validateSuspensionPressure(
        pressure: Double,
        isFrontSuspension: Boolean
    ): SubResult {
        if (pressure <= 0.0) return SubResult.FAILURE
        return if (pressure < if (isFrontSuspension) 18.0 else 25.0) SubResult.SUCCESS else SubResult.OUTLIER
    }

    private fun validateSuspensionSetup(
        lsc: Int?,
        hsc: Int?,
        lsr: Int?,
        hsr: Int?
    ): SubResult {
        return if (lsc == null || lsr == null) {
            SubResult.FAILURE
        } else {
            return if (validateClicks(lsc) &&
                validateClicks(lsr) &&
                hsc?.let { validateClicks(it) } != false &&
                hsr?.let { validateClicks(it) } != false
            ) {
                SubResult.SUCCESS
            } else {
                SubResult.OUTLIER
            }
        }
    }

    private fun validateClicks(clicks: Int): Boolean {
        return clicks in 1..29
    }

    object SetupFailure : Result.Failure()

    object SetupOutlier : Result.Success()

    enum class SubResult {
        SUCCESS,
        OUTLIER,
        FAILURE
    }
}