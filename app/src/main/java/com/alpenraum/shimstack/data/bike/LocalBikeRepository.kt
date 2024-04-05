package com.alpenraum.shimstack.data.bike

import javax.inject.Inject

class LocalBikeRepository
@Inject
constructor(private val bikeDAO: BikeDAO) : BikeRepository {
    override suspend fun createBike(bikeDTO: BikeDTO) {
        bikeDAO.insertBike(bikeDTO)
    }

    override suspend fun getAllBikes(): List<BikeDTO> {
        return bikeDAO.getAllBikes()
    }

    override suspend fun getBike(id: Int): BikeDTO? {
        return bikeDAO.getBike(id)
    }

    override suspend fun updateBike(bikeDTO: BikeDTO) {
        return bikeDAO.updateBike(bikeDTO)
    }
}