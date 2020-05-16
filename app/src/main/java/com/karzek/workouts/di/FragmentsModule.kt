package com.karzek.workouts.di

import com.karzek.exercises.di.ExercisesFragments
import dagger.Module

@Module(
    includes = [
        ExercisesFragments::class
    ]
)
interface FragmentsModule