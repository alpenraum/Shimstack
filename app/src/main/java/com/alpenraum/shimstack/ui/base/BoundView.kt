package com.example.opensky.ui.base

interface BoundView<VIEW_MODEL : BaseViewModel> {
    val viewModel: VIEW_MODEL
    val viewModelClass: Class<VIEW_MODEL>?

    fun onViewModelBound()
}
