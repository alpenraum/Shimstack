package com.alpenraum.shimstack.data.bikeTemplates

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alpenraum.shimstack.data.db.AppDatabase
import com.alpenraum.shimstack.data.models.biketemplate.BikeTemplateDTO

@Dao
interface BikeTemplateDAO {
    @Insert
    fun insertBike(bikes: List<BikeTemplateDTO>)

    @Query(
        "SELECT * FROM ${AppDatabase.TABLE_BIKE_TEMPLATE} WHERE name LIKE '%' || :searchTerm || '%'"
    )
    fun getBikeTemplatesFilteredByName(searchTerm: String?): List<BikeTemplateDTO>
}