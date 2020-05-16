package com.karzek.workouts.di

import com.karzek.core.di.CoreModule
import com.karzek.workouts.WorkoutApplication
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        CoreModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<WorkoutApplication> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<WorkoutApplication>
}