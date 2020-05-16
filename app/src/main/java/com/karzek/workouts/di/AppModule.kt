package com.karzek.workouts.di

import android.content.Context
import com.karzek.workouts.WorkoutApplication
import dagger.Module
import dagger.Provides

@Module
object AppModule {

    @Provides
    fun provideApplicationContext(application: WorkoutApplication): Context = application.applicationContext

}