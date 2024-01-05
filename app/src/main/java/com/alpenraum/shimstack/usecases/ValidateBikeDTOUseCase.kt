package com.alpenraum.shimstack.usecases

import android.util.Log
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDTO
import com.alpenraum.shimstack.ui.features.newBike.DetailsInputData
import javax.inject.Inject

class ValidateBikeDTOUseCase
@Inject
constructor() {
    operator fun invoke(bikeDTO: BikeDTO): Result {
        return fromResults(
            validateName(bikeDTO.name),
            validateType(bikeDTO.type),
            validateTireWidth(bikeDTO.frontTire.widthInMM) &&
                bikeDTO.frontTire.internalRimWidthInMM?.let {
                    validateInternalRimWidth(
                        it
                    )
                } ?: true,
            validateTireWidth(bikeDTO.rearTire.widthInMM) &&
                bikeDTO.rearTire.internalRimWidthInMM?.let {
                    validateInternalRimWidth(
                        it
                    )
                } ?: true,
            bikeDTO.frontSuspension?.travel?.let { validateSuspensionTravel(it) } ?: true,
            bikeDTO.rearSuspension?.travel?.let { validateSuspensionTravel(it) } ?: true
        )
    }

    operator fun invoke(
        data: DetailsInputData,
        type: Bike.Type?
    ): Result {
        return fromResults(
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
    }

    private fun validateName(name: String?): Boolean {
        return name?.isNotBlank() ?: false
    }

    private fun validateType(type: Bike.Type): Boolean {
        Log.d("___", "validateType: $type")
        return type != Bike.Type.UNKNOWN
    }

    private fun validateTireWidth(tireWidth: Double?): Boolean {
        return if (tireWidth == null) {
            false
        } else {
            tireWidth > 0.0 && tireWidth < 150.0
        }
    }

    private fun validateInternalRimWidth(internalRimWidth: Double?): Boolean {
        return if (internalRimWidth == null || internalRimWidth == 0.0) {
            true
        } else {
            internalRimWidth > 0.0 && internalRimWidth < 100.0
        }
    }

    private fun validateSuspensionTravel(travel: Int): Boolean {
        return travel > 0.0
    }

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
    ) : Result.Failure()

    private fun fromResults(
        name: Boolean,
        type: Boolean,
        frontTire: Boolean,
        rearTire: Boolean,
        frontSuspension: Boolean,
        rearSuspension: Boolean
    ): Result {
        return if (name && type && frontTire && rearTire && frontSuspension && rearSuspension) {
            Result.Success()
        } else {
            DetailsFailure(name, type, frontTire, rearTire, frontSuspension, rearSuspension)
        }
    }
}