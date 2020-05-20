package com.karzek.exercises.di

import com.karzek.exercises.data.exercise.ExerciseLocalDataSource
import com.karzek.exercises.data.exercise.ExerciseRemoteDataSource
import com.karzek.exercises.data.exercise.PagedExerciseProvider
import com.karzek.exercises.data.exercise.contract.IExerciseLocalDataSource
import com.karzek.exercises.data.exercise.contract.IExerciseRemoteDataSource
import com.karzek.exercises.domain.exercise.repository.IPagedExerciseProvider
import com.karzek.exercises.domain.validation.IValidateCacheUseCase
import com.karzek.exercises.domain.validation.ValidateCacheUseCase
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        StorageModule::class,
        ExercisesApiModule::class,
        MuscleModule::class,
        CategoryModule::class,
        EquipmentModule::class,
        ExerciseImageModule::class
    ]
)
interface ExercisesModule {

    @Binds
    fun bindValidateCacheUseCase(validateCacheUseCase: ValidateCacheUseCase): IValidateCacheUseCase

    @Binds
    fun bindPagedExerciseProvider(exerciseRepository: PagedExerciseProvider): IPagedExerciseProvider

    @Binds
    fun bindExerciseRemoteDataSource(exerciseRemoteDataSource: ExerciseRemoteDataSource): IExerciseRemoteDataSource

    @Binds
    fun bindExerciseLocalDataSource(exerciseLocalDataSource: ExerciseLocalDataSource): IExerciseLocalDataSource

}