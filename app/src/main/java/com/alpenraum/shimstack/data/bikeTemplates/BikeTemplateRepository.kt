package com.alpenraum.shimstack.data.bikeTemplates

import android.content.Context
import com.alpenraum.shimstack.data.models.biketemplate.BikeTemplate

interface BikeTemplateRepository {
    suspend fun prepopulateData(context: Context)

    suspend fun createBikeTemplates(list: List<BikeTemplate>)

    suspend fun getBikeTemplatesFilteredByName(name: String): List<BikeTemplate>
}