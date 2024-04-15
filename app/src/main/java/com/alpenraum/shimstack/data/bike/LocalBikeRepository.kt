package com.alpenraum.shimstack.data.bike

import com.alpenraum.shimstack.core.database.dao.BikeDAO
import com.alpenraum.shimstack.data.models.bike.Bike
import com.alpenraum.shimstack.data.toDTO
import javax.inject.Inject

class LocalBikeRepository
    @Inject
    constructor(private val bikeDAO: BikeDAO) : BikeRepository {
        override suspend fun createBike(bike: Bike) {
            bikeDAO.insertBike(bike.toDTO())
        }

        override suspend fun getAllBikes(): List<Bike> {
            return bikeDAO.getAllBikes().map { Bike.fromDto(it) }
        }

        override suspend fun getBike(id: Int): Bike? {
            return bikeDAO.getBike(id)?.let { Bike.fromDto(it) }
        }

        override suspend fun updateBike(bike: Bike) {
            return bikeDAO.updateBike(bike.toDTO())
        }
    }