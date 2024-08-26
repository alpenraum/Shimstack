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
                validateTireWidth(bike.frontTire.widthInMM) &&
                    bike.frontTire.internalRimWidthInMM?.let {
                        validateInternalRimWidth(
                            it
                        )
                    } ?: true,
                validateTireWidth(bike.rearTire.widthInMM) &&
                    bike.rearTire.internalRimWidthInMM?.let {
                        validateInternalRimWidth(
                            it
                        )
                    } ?: true,
                bike.frontSuspension?.travel?.let { validateSuspensionTravel(it) } ?: true,
                bike.rearSuspension?.travel?.let { validateSuspensionTravel(it) } ?: true
            )

        operator fun invoke(
            data: DetailsInputData,
            type: BikeType?
        ): Result<DetailsFailure?> =
            fromResults(
                validateName(data.name),
                type?.let { validateType(it) } ?: false,
                validateTireWidth(data.frontTireWidth?.toDoubleOrNull()) &&
                    data.frontInternalRimWidth?.let {
                        validateInternalRimWidth(
                            it.toDoubleOrNull()
                        )
                    } ?: true,
                validateTireWidth(data.rearTireWidth?.toDoubleOrNull()) &&
                    data.rearInternalRimWidth?.let {
                        validateInternalRimWidth(
                            it.toDoubleOrNull()
                        )
                    } ?: true,
                data.frontTravel?.toIntOrNull()?.let { validateSuspensionTravel(it) } ?: true,
                data.rearTravel?.toIntOrNull()?.let { validateSuspensionTravel(it) } ?: true
            )

        private fun validateName(name: String?): Boolean = name?.isNotBlank() ?: false

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