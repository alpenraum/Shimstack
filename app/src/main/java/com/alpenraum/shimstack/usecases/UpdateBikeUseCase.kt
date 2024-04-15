package com.alpenraum.shimstack.usecases

import android.util.Log
import com.alpenraum.shimstack.data.bike.BikeRepository
import com.alpenraum.shimstack.data.models.bike.Bike
import javax.inject.Inject

class UpdateBikeUseCase
    @Inject
    constructor(private val bikeRepository: BikeRepository) {
        suspend operator fun invoke(bike: Bike): Boolean {
            return try {
                val id = bike.id ?: return false
                val currentBike = bikeRepository.getBike(id) ?: return false
                val update =
                    currentBike.copy(
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
                Log.d("UpdateBikeUseCase", "invoke: $e")
                false
            }
        }
    }