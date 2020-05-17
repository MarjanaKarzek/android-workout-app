package com.karzek.exercises.base

import androidx.annotation.CallSuper
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [MockKExtension::class])
open class BaseUnitTest {

    @BeforeEach
    @CallSuper
    open fun setup() {
        clearAllMocks()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
    }

    @AfterEach
    open fun cleanup() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }
}