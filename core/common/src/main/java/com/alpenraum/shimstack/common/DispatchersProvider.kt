package com.alpenraum.shimstack.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {
    /**
     * I/O dispatcher - Optimized for disk and network IO off the main thread
     */
    val io: CoroutineDispatcher

    /**
     * Main dispatcher - interacts with UI
     */
    val main: CoroutineDispatcher

    /**
     * Default dispatcher - Optimized for CPU intensive work off the main thread
     */
    val computation: CoroutineDispatcher

    /**
     * The Dispatchers.Unconfined coroutine dispatcher starts coroutine in the caller thread,
     * but only until the first suspension point.
     * After suspension it resumes in the thread that is fully determined by
     * the suspending function that was invoked.
     */
    val unconfined: CoroutineDispatcher
}

class DefaultDispatchersProvider : DispatchersProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO

    override val main: CoroutineDispatcher = Dispatchers.Main.immediate

    override val computation: CoroutineDispatcher = Dispatchers.Default

    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}