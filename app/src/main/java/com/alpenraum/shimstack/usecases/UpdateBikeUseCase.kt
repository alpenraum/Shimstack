package com.alpenraum.shimstack.usecases

import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeRepository
import javax.inject.Inject

class UpdateBikeUseCase @Inject constructor(private val bikeRepository: BikeRepository) {

    suspend operator fun invoke(bike: Bike): Boolean {
        return try {
            val bikeDto = bikeRepository.getBike(bike.id) ?: return false
            val update = bikeDto.copy(
                name = bike.name,
                type = bike.type,
                frontTire = bike.frontTire,
                rearTire = bike.rearTire,
                frontSuspension = bike.frontSuspension,
                rearSuspension = bike.rearSuspension
            )

            bikeRepository.updateBike(update)

            true
        } catch (e: Exception) {
            false
        }
    }
}