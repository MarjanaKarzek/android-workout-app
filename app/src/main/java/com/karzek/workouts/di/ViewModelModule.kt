package com.karzek.workouts.di

import androidx.lifecycle.ViewModelProvider
import com.karzek.core.di.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module(includes = [])
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}