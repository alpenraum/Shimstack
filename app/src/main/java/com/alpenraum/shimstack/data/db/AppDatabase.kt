package com.alpenraum.shimstack.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDAO
import com.alpenraum.shimstack.data.db.typeconverter.BigDecimalTypeConverter

@Database(entities = [Bike::class], version = 1, exportSchema = false)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bikeDao(): BikeDAO
}
