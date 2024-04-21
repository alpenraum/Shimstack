package com.alpenraum.shimstack.base

import com.alpenraum.shimstack.common.DispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatchersProvider(
    testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : DispatchersProvider {
    override val io = testDispatcher

    override val main = testDispatcher

    override val computation = testDispatcher

    override val unconfined = testDispatcher
}