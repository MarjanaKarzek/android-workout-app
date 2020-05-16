package com.karzek.workouts.di

import androidx.lifecycle.ViewModelProvider
import com.karzek.core.di.DaggerViewModelFactory
import com.karzek.exercises.di.ExercisesViewModels
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        ExercisesViewModels::class
    ]
)
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}