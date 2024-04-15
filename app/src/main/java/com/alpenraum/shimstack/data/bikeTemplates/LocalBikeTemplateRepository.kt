package com.alpenraum.shimstack.data.bikeTemplates

import android.content.Context
import com.alpenraum.shimstack.core.database.dao.BikeTemplateDAO
import com.alpenraum.shimstack.core.database.models.BikeTemplateDTO
import com.alpenraum.shimstack.data.models.biketemplate.BikeTemplate
import com.alpenraum.shimstack.data.toDomain
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import javax.inject.Inject

class LocalBikeTemplateRepository
    @Inject
    constructor(
        private val bikeTemplateDAO: BikeTemplateDAO,
        private val moshi: Moshi
    ) : BikeTemplateRepository {
        @OptIn(ExperimentalStdlibApi::class)
        override suspend fun prepopulateData(context: Context) {
            val adapter: JsonAdapter<List<BikeTemplateDTO>> = moshi.adapter()
            val file = "bike_templates.json"
            val json = context.assets.open(file).bufferedReader().use { it.readText() }

            adapter.fromJson(json)?.let { createBikeTemplates(it) }
        }

        private fun createBikeTemplates(list: List<BikeTemplateDTO>) {
            bikeTemplateDAO.insertBike(list)
        }

        override suspend fun createBikeTemplates(list: List<BikeTemplate>) {
            createBikeTemplates(list.map { it.toDTO() })
        }

        override suspend fun getBikeTemplatesFilteredByName(name: String): List<BikeTemplate> {
            return bikeTemplateDAO.getBikeTemplatesFilteredByName(name).map { it.toDomain() }
        }
    }