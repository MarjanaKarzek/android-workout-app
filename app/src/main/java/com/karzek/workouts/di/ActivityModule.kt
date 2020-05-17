package com.karzek.workouts.di

import com.karzek.core.di.ActivityScope
import com.karzek.exercises.di.ExercisesActivities
import com.karzek.workouts.ui.main.MainActivity
import com.karzek.workouts.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ExercisesActivities::class])
interface ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    @ActivityScope
    fun provideMainActivity(): MainActivity

    @ContributesAndroidInjector
    @ActivityScope
    fun provideSplashActivity(): SplashActivity
}