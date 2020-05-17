package com.karzek.exercises.di

import com.karzek.core.di.ActivityScope
import com.karzek.exercises.ui.detail.ExerciseDetailsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ExercisesActivities {

    @ContributesAndroidInjector
    @ActivityScope
    fun provideExerciseDetailsActivity(): ExerciseDetailsActivity
}