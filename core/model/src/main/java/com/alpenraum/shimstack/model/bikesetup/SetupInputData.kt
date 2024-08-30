package com.alpenraum.shimstack.model.bikesetup

data class SetupInputData(
    val frontTirePressure: String? = null,
    val rearTirePressure: String? = null,
    val frontSuspensionPressure: String? = null,
    val rearSuspensionPressure: String? = null,
    val frontSuspensionTokens: String? = null,
    val rearSuspensionTokens: String? = null,
    val frontSuspensionLSC: String? = null,
    val frontSuspensionHSC: String? = null,
    val frontSuspensionLSR: String? = null,
    val frontSuspensionHSR: String? = null,
    val rearSuspensionLSC: String? = null,
    val rearSuspensionHSC: String? = null,
    val rearSuspensionLSR: String? = null,
    val rearSuspensionHSR: String? = null
)