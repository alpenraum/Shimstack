package com.alpenraum.shimstack.base

import com.alpenraum.shimstack.common.DispatchersProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

abstract class BaseTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
}

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTestRule(
    private var testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    val testDispatchersProvider: DispatchersProvider = TestDispatchersProvider(testDispatcher)

    override fun starting(description: Description) {
        super.starting(description)
        try {
            Dispatchers.setMain(testDispatcher)
        } catch (e: Exception) {
            testDispatcher = UnconfinedTestDispatcher()
            Dispatchers.setMain(testDispatcher)
        }
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
        super.finished(description)
    }
}