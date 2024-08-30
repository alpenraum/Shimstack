package com.alpenraum.shimstack.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alpenraum.shimstack.core.database.db.AppDatabase
import com.alpenraum.shimstack.core.database.models.BikeTemplateDTO

@Dao
interface BikeTemplateDAO {
    @Insert
    fun insertBike(bikes: List<BikeTemplateDTO>)

    @Query(
        "SELECT * FROM ${AppDatabase.TABLE_BIKE_TEMPLATE} WHERE name LIKE '%' || :searchTerm || '%'"
    )
    fun getBikeTemplatesFilteredByName(searchTerm: String?): List<BikeTemplateDTO>
}