package com.alpenraum.shimstack.ui.features.main

import com.alpenraum.shimstack.common.stores.KeyValueStore
import com.alpenraum.shimstack.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val keyValueStore: KeyValueStore
) : BaseViewModel()