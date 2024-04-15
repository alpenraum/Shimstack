package com.alpenraum.shimstack.data.bike

import com.alpenraum.shimstack.data.models.bike.Bike

interface BikeRepository {
    suspend fun createBike(bike: Bike)

    suspend fun getAllBikes(): List<Bike>

    suspend fun getBike(id: Int): Bike?

    suspend fun updateBike(bike: Bike)
}