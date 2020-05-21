package com.karzek.workouts.di

import com.karzek.workouts.WorkoutApplication
import dagger.Dagger
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TestWorkoutApplication : WorkoutApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        Dagger.factory(TestAppComponent.Factory::class.java).create(this)

}