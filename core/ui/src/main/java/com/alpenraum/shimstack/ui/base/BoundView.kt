package com.example.opensky.ui.base

import com.alpenraum.shimstack.ui.base.BaseViewModel

interface BoundView<VIEW_MODEL : BaseViewModel> {
    val viewModel: VIEW_MODEL
    val viewModelClass: Class<VIEW_MODEL>?

    fun onViewModelBound()
}