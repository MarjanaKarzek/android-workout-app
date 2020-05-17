package com.karzek.exercises.di

import com.karzek.exercises.data.ExerciseRemoteDataSource
import com.karzek.exercises.data.ExerciseRepository
import com.karzek.exercises.data.IExerciseRemoteDataSource
import com.karzek.exercises.domain.GetExercisesUseCase
import com.karzek.exercises.domain.IGetExercisesUseCase
import com.karzek.exercises.domain.repository.IExerciseRepository
import dagger.Binds
import dagger.Module

@Module(includes = [ExercisesApiModule::class])
interface ExercisesModule {

    @Binds
    fun bindGetExercisesUseCase(getExercisesUseCase: GetExercisesUseCase): IGetExercisesUseCase

    @Binds
    fun bindExerciseRepository(exerciseRepository: ExerciseRepository): IExerciseRepository

    @Binds
    fun bindExerciseRemoteDataSource(exerciseRemoteDataSource: ExerciseRemoteDataSource): IExerciseRemoteDataSource

}