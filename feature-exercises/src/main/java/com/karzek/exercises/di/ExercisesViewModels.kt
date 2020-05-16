package com.karzek.exercises.di

import androidx.lifecycle.ViewModel
import com.karzek.core.di.ViewModelKey
import com.karzek.exercises.ui.ExercisesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ExercisesViewModels {
    @Binds
    @IntoMap
    @ViewModelKey(ExercisesViewModel::class)
    fun bindDailyViewModel(viewModel: ExercisesViewModel): ViewModel
}