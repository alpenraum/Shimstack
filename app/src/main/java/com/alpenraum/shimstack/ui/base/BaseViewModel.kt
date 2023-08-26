package com.alpenraum.shimstack.ui.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    var arguments: Bundle? = null

    protected val viewModelScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main + SupervisorJob(null))
    }

    protected val iOScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.IO + SupervisorJob(null))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        iOScope.cancel()
    }

    open fun onStart() {}
    open fun onStop() {}
}
