package com.alpenraum.shimstack.data.bikeTemplates

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.db.AppDatabase

@Entity(tableName = AppDatabase.table_bike_template)
class BikeTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val type: Bike.Type,
    val isEBike: Boolean,
    val frontSuspensionTravelInMM: Int,
    val rearSuspensionTravelInMM: Int
)
