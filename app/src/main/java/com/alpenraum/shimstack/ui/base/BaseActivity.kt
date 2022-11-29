package com.example.opensky.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity<VIEW_MODEL : BaseViewModel> :
    ComponentActivity(),
    BoundView<VIEW_MODEL> {

    private lateinit var _viewModel: VIEW_MODEL

    override val viewModel: VIEW_MODEL
        get() = _viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory
        )[viewModelClass as Class<VIEW_MODEL>]

        lifecycle.addObserver(_viewModel)

        intent.extras?.let {
            _viewModel.arguments = it
        }
        onViewModelBound()
    }
}
