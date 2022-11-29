package com.alpenraum.shimstack.data.bike

import javax.inject.Inject

class LocalBikeRepository @Inject constructor(private val bikeDAO: BikeDAO) : BikeRepository {
    override suspend fun createBike(bike: Bike): Bike {
        return bikeDAO.insertBike(bike)
    }

    override suspend fun getAllBikes(): List<Bike> {
        return bikeDAO.getAllBikes()
    }
}
