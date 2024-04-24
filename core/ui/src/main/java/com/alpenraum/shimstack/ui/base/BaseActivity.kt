package com.alpenraum.shimstack.ui.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.opensky.ui.base.BoundView

abstract class BaseActivity<VIEW_MODEL : BaseViewModel> :
    FragmentActivity(),
    BoundView<VIEW_MODEL> {
    private lateinit var _viewModel: VIEW_MODEL

    override val viewModel: VIEW_MODEL
        get() = _viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewModel =
            ViewModelProvider(
                this,
                defaultViewModelProviderFactory
            )[viewModelClass as Class<VIEW_MODEL>]

        intent.extras?.let {
            _viewModel.arguments = it
        }
        onViewModelBound()
    }
}