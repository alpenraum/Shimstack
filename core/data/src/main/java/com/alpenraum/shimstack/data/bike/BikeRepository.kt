package com.alpenraum.shimstack.data.bike

import com.alpenraum.shimstack.model.bike.Bike
import kotlinx.coroutines.flow.Flow

interface BikeRepository {
    suspend fun createBike(bike: Bike)

    fun getAllBikes(): Flow<List<Bike>>

    suspend fun getBike(id: Int): Bike?

    suspend fun updateBike(bike: Bike)
}