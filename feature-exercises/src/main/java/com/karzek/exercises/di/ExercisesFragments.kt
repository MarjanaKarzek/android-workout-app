package com.karzek.exercises.di

import com.karzek.core.di.FragmentScope
import com.karzek.exercises.ui.overview.ExercisesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ExercisesFragments {

    @FragmentScope
    @ContributesAndroidInjector
    fun provideExerciseListFragment(): ExercisesFragment
}