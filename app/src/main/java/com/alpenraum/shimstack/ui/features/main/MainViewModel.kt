package com.alpenraum.shimstack.ui.features.main

import android.content.Context
import com.alpenraum.shimstack.common.DispatchersProvider
import com.alpenraum.shimstack.data.bikeTemplates.LocalBikeTemplateRepository
import com.alpenraum.shimstack.datastore.ShimstackDatastore
import com.alpenraum.shimstack.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val bikeTemplateRepository: LocalBikeTemplateRepository,
        private val datastore: ShimstackDatastore,
        dispatchersProvider: DispatchersProvider
    ) : BaseViewModel(dispatchersProvider) {
        fun onBound(context: Context) {
            iOScope.launch {
                datastore.isOnboardingCompleted.collect {
                    if (!it) {
                        bikeTemplateRepository.prepopulateData(context)
                    }
                }
            }
        }
    }