package com.alpenraum.shimstack.data.bike

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.alpenraum.shimstack.data.db.AppDatabase
import com.alpenraum.shimstack.data.models.bike.BikeDTO

@Dao
interface BikeDAO {
    @Query("SELECT * FROM ${AppDatabase.TABLE_BIKE}")
    fun getAllBikes(): List<BikeDTO>

    @Insert
    fun insertBike(bikeDTO: BikeDTO)

    @Query("SELECT * FROM ${AppDatabase.TABLE_BIKE} WHERE id = :id LIMIT 1")
    fun getBike(id: Int): BikeDTO?

    @Update
    fun updateBike(bikeDTO: BikeDTO)
}