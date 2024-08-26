package com.alpenraum.shimstack.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatchersProvider(
    testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : com.alpenraum.shimstack.common.DispatchersProvider {
    override val io = testDispatcher

    override val main = testDispatcher

    override val computation = testDispatcher

    override val unconfined = testDispatcher
}