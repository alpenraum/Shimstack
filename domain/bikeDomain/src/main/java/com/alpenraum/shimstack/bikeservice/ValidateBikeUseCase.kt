package com.alpenraum.shimstack.home.usecases

import com.alpenraum.shimstack.model.bike.Bike
import com.alpenraum.shimstack.model.bike.BikeType
import com.alpenraum.shimstack.model.bikesetup.DetailsInputData
import javax.inject.Inject

class ValidateBikeUseCase
@Inject
constructor() {
    operator fun invoke(bike: Bike): Result<DetailsFailure?> =
        fromResults(
            validateName(bike.name),
            validateType(bike.type),
            validateTireWidth(bike.frontTire.width.asMetric().toDouble()) &&
                    bike.frontTire.internalRimWidthInMM?.let {
                        validateInternalRimWidth(
                            it.asMetric().toDouble()
                        )
                    } != false,
            validateTireWidth(bike.rearTire.width.asMetric().toDouble()) &&
                    bike.rearTire.internalRimWidthInMM?.let {
                        validateInternalRimWidth(
                            it.asMetric().toDouble()
                        )
                    } != false,
            bike.frontSuspension?.travel?.let { validateSuspensionTravel(it.asMetric().toInt()) } != false,
            bike.rearSuspension?.travel?.let { validateSuspensionTravel(it.asMetric().toInt()) } != false
        )

    operator fun invoke(
        data: DetailsInputData,
        type: BikeType?
    ): Result<DetailsFailure?> =
        fromResults(
            validateName(data.name),
            type?.let { validateType(it) } == true,
            validateTireWidth(data.frontTireWidth?.toDoubleOrNull()) &&
                    data.frontInternalRimWidth?.let {
                        validateInternalRimWidth(
                            it.toDoubleOrNull()
                        )
                    } != false,
            validateTireWidth(data.rearTireWidth?.toDoubleOrNull()) &&
                    data.rearInternalRimWidth?.let {
                        validateInternalRimWidth(
                            it.toDoubleOrNull()
                        )
                    } != false,
            data.frontTravel?.toIntOrNull()?.let { validateSuspensionTravel(it) } != false,
            data.rearTravel?.toIntOrNull()?.let { validateSuspensionTravel(it) } != false
        )

    private fun validateName(name: String?): Boolean = name?.isNotBlank() == true

    private fun validateType(type: BikeType): Boolean = type != BikeType.UNKNOWN

    private fun validateTireWidth(tireWidth: Double?): Boolean =
        if (tireWidth == null) {
            false
        } else {
            tireWidth > 0.0 && tireWidth < 150.0
        }

    private fun validateInternalRimWidth(internalRimWidth: Double?): Boolean =
        if (internalRimWidth == null || internalRimWidth == 0.0) {
            true
        } else {
            internalRimWidth > 0.0 && internalRimWidth < 100.0
        }

    private fun validateSuspensionTravel(travel: Int): Boolean = travel > 0.0

    /**
     * False => Validation failed
     * True => Validation success*/
    data class DetailsFailure(
        val name: Boolean,
        val type: Boolean,
        val frontTire: Boolean,
        val rearTire: Boolean,
        val frontSuspension: Boolean,
        val rearSuspension: Boolean
    )

    private fun fromResults(
        name: Boolean,
        type: Boolean,
        frontTire: Boolean,
        rearTire: Boolean,
        frontSuspension: Boolean,
        rearSuspension: Boolean
    ): Result<DetailsFailure?> =
        if (name && type && frontTire && rearTire && frontSuspension && rearSuspension) {
            Result.success(null)
        } else {
            Result.success(DetailsFailure(name, type, frontTire, rearTire, frontSuspension, rearSuspension))
        }
}