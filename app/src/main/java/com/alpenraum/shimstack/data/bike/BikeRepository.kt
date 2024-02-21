package com.alpenraum.shimstack.data.bike

interface BikeRepository {
    suspend fun createBike(bikeDTO: BikeDTO)

    suspend fun getAllBikes(): List<BikeDTO>

    suspend fun getBike(id: Int): BikeDTO?

    suspend fun updateBike(bikeDTO: BikeDTO)
}