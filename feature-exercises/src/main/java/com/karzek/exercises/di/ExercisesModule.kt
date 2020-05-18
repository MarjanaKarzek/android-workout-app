package com.karzek.exercises.di

import com.karzek.exercises.data.exercise.ExerciseRemoteDataSource
import com.karzek.exercises.data.exercise.ExerciseRepository
import com.karzek.exercises.data.exercise.contract.IExerciseRemoteDataSource
import com.karzek.exercises.domain.exercise.GetExercisesUseCase
import com.karzek.exercises.domain.exercise.GetImagesForExerciseUseCase
import com.karzek.exercises.domain.exercise.IGetExercisesUseCase
import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase
import com.karzek.exercises.domain.exercise.repository.IExerciseRepository
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        StorageModule::class,
        ExercisesApiModule::class,
        MuscleModule::class,
        CategoryModule::class
    ]
)
interface ExercisesModule {

    @Binds
    fun bindGetExercisesUseCase(getExercisesUseCase: GetExercisesUseCase): IGetExercisesUseCase

    @Binds
    fun bindGetImagesForExerciseUseCase(getImagesForExerciseUseCase: GetImagesForExerciseUseCase): IGetImagesForExerciseUseCase

    @Binds
    fun bindExerciseRepository(exerciseRepository: ExerciseRepository): IExerciseRepository

    @Binds
    fun bindExerciseRemoteDataSource(exerciseRemoteDataSource: ExerciseRemoteDataSource): IExerciseRemoteDataSource

}