package com.alpenraum.shimstack.data.bike

interface BikeRepository {
    suspend fun createBike(bike: Bike): Bike

    suspend fun getAllBikes(): List<Bike>
}
