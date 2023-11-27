package com.alpenraum.shimstack.usecases.biometrics

import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDTO
import javax.inject.Inject

class ValidateBikeDTOUseCase @Inject constructor() {

    operator fun invoke(bikeDTO: BikeDTO): Result {
        return Result.fromResults(
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

    private fun validateName(name: String): Boolean {
        return name.isNotBlank()
    }

    private fun validateType(type: Bike.Type): Boolean {
        return type != Bike.Type.UNKNOWN
    }

    private fun validateTireWidth(tireWidth: Double): Boolean {
        return tireWidth > 0.0 && tireWidth < 150.0
    }

    private fun validateInternalRimWidth(internalRimWidth: Double): Boolean {
        return if (internalRimWidth == 0.0) {
            true
        } else {
            internalRimWidth > 0.0 && internalRimWidth < 100.0
        }
    }

    private fun validateSuspensionTravel(travel: Int): Boolean {
        return travel > 0.0
    }

    sealed class Result {
        data object Success : Result()

        /**
         * False => Validation failed
         * True => Validation success*/
        data class Failure(
            val name: Boolean,
            val type: Boolean,
            val frontTire: Boolean,
            val rearTire: Boolean,
            val frontSuspension: Boolean,
            val rearSuspension: Boolean
        ) : Result()

        fun isSuccess() = this is Success

        companion object {
            fun fromResults(
                name: Boolean,
                type: Boolean,
                frontTire: Boolean,
                rearTire: Boolean,
                frontSuspension: Boolean,
                rearSuspension: Boolean
            ): Result {
                return if (name && type && frontTire && rearTire && frontSuspension && rearSuspension) {
                    Success
                } else {
                    Failure(name, type, frontTire, rearTire, frontSuspension, rearSuspension)
                }
            }
        }
    }
}
