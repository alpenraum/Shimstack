package com.alpenraum.shimstack.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDAO

@Database(entities = [Bike::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bikeDao(): BikeDAO
}
