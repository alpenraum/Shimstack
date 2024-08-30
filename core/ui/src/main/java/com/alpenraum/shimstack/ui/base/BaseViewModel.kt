package com.alpenraum.shimstack.ui.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.alpenraum.shimstack.common.DispatchersProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BaseViewModel(
    protected val dispatchersProvider: DispatchersProvider
) : ViewModel() {
    var arguments: Bundle? = null

    protected val iOScope: CoroutineScope by lazy {
        CoroutineScope(dispatchersProvider.io + SupervisorJob(null))
    }
    protected val viewModelScope: CoroutineScope by lazy {
        CoroutineScope(dispatchersProvider.main + SupervisorJob(null))
    }

    override fun onCleared() {
        super.onCleared()
        iOScope.cancel()
    }

    open fun onStart() {}

    open fun onStop() {}
}