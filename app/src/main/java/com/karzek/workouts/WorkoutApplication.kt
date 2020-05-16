package com.karzek.workouts

import com.karzek.workouts.di.AppComponent
import dagger.Dagger
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class WorkoutApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        Dagger.factory(AppComponent.Factory::class.java).create(this)

}