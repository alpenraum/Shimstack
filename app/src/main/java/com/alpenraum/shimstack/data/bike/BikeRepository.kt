package com.alpenraum.shimstack.data.bike

import com.alpenraum.shimstack.data.models.bike.BikeDTO

interface BikeRepository {
    suspend fun createBike(bikeDTO: BikeDTO)

    suspend fun getAllBikes(): List<BikeDTO>

    suspend fun getBike(id: Int): BikeDTO?

    suspend fun updateBike(bikeDTO: BikeDTO)
}