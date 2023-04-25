package com.alpenraum.shimstack.ui.base

import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseViewModel :ViewModel(), DefaultLifecycleObserver {

    var arguments: Bundle? = null

    protected val viewModelScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main + SupervisorJob(null))
    }

    protected val iOScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.IO + SupervisorJob(null))
    }
}
