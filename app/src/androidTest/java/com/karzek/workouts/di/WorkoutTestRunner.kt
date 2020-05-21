package com.karzek.workouts.di

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import androidx.test.runner.AndroidJUnitRunner
import com.github.tmurakami.dexopener.DexOpener
import io.appflate.restmock.RESTMockServerStarter
import io.appflate.restmock.android.AndroidAssetsFileParser
import io.appflate.restmock.android.AndroidLogger

class WorkoutTestRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle) {
        StrictMode.setThreadPolicy(
            Builder().permitAll()
                .build()
        )
        super.onCreate(arguments)
        RESTMockServerStarter.startSync(AndroidAssetsFileParser(context), AndroidLogger())
    }

    @Throws(
        InstantiationException::class, IllegalAccessException::class,
        ClassNotFoundException::class
    ) override fun newApplication(
        cl: ClassLoader,
        className: String,
        context: Context
    ): Application {
        DexOpener.install(this)
        return super.newApplication(cl, "com.karzek.workouts.di.TestWorkoutApplication", context)
    }
}