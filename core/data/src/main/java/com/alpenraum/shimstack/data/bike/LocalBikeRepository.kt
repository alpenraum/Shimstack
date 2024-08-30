package com.alpenraum.shimstack.data.bike

import com.alpenraum.shimstack.core.database.dao.BikeDAO
import com.alpenraum.shimstack.data.toDTO
import com.alpenraum.shimstack.data.toDomain
import com.alpenraum.shimstack.model.bike.Bike
import javax.inject.Inject

class LocalBikeRepository
    @Inject
    constructor(private val bikeDAO: BikeDAO) : BikeRepository {
        override suspend fun createBike(bike: Bike) {
            bikeDAO.insertBike(bike.toDTO())
        }

        override suspend fun getAllBikes(): List<Bike> {
            return bikeDAO.getAllBikes().map { it.toDomain() }
        }

        override suspend fun getBike(id: Int): Bike? {
            return bikeDAO.getBike(id)?.toDomain()
        }

        override suspend fun updateBike(bike: Bike) {
            return bikeDAO.updateBike(bike.toDTO())
        }
    }