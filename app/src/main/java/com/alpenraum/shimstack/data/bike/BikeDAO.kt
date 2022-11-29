package com.alpenraum.shimstack.data.bike

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BikeDAO {

    @Query("SELECT * FROM Bike")
    fun getAllBikes(): List<Bike>

    @Insert
    fun insertBike(bike: Bike): Bike
}
