package com.karzek.workouts

import com.karzek.core.reporting.CrashReportingTree
import com.karzek.workouts.di.AppComponent
import dagger.Dagger
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import timber.log.Timber.DebugTree

open class WorkoutApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        Dagger.factory(AppComponent.Factory::class.java).create(this)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }
}