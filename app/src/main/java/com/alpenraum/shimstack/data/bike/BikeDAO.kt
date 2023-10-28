package com.alpenraum.shimstack.data.bike

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alpenraum.shimstack.data.db.AppDatabase

@Dao
interface BikeDAO {

    @Query("SELECT * FROM ${AppDatabase.table_bike}")
    fun getAllBikes(): List<Bike>

    @Insert
    fun insertBike(bike: Bike)
}
