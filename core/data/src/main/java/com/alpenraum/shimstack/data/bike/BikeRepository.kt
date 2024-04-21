package com.alpenraum.shimstack.data.bike

interface BikeRepository {
    suspend fun createBike(bike: com.alpenraum.shimstack.model.bike.Bike)

    suspend fun getAllBikes(): List<com.alpenraum.shimstack.model.bike.Bike>

    suspend fun getBike(id: Int): com.alpenraum.shimstack.model.bike.Bike?

    suspend fun updateBike(bike: com.alpenraum.shimstack.model.bike.Bike)
}