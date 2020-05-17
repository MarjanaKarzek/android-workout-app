package com.karzek.exercises.di

import androidx.lifecycle.ViewModel
import com.karzek.core.di.ViewModelKey
import com.karzek.exercises.ui.detail.ExerciseDetailsViewModel
import com.karzek.exercises.ui.overview.ExercisesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ExercisesViewModels {

    @Binds
    @IntoMap
    @ViewModelKey(ExercisesViewModel::class)
    fun bindExercisesViewModel(viewModel: ExercisesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExerciseDetailsViewModel::class)
    fun bindExerciseDetailsViewModel(viewModel: ExerciseDetailsViewModel): ViewModel

}