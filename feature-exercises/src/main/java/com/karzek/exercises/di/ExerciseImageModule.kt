package com.karzek.exercises.di

import com.karzek.exercises.data.exerciseImages.ExerciseImageRepository
import com.karzek.exercises.data.exerciseImages.contract.ExerciseImageRemoteDataSource
import com.karzek.exercises.data.exerciseImages.contract.IExerciseImageRemoteDataSource
import com.karzek.exercises.domain.exerciseimage.GetImagesForExerciseUseCase
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase
import com.karzek.exercises.domain.exerciseimage.repository.IExerciseImageRepository
import dagger.Binds
import dagger.Module

@Module
interface ExerciseImageModule {

    @Binds
    fun bindGetImagesForExerciseUseCase(getImagesForExerciseUseCase: GetImagesForExerciseUseCase): IGetImagesForExerciseUseCase

    @Binds
    fun bindExerciseImageRepository(exerciseImageRepository: ExerciseImageRepository): IExerciseImageRepository

    @Binds
    fun bindExerciseImageRemoteDataSource(exerciseImageRemoteDataSource: ExerciseImageRemoteDataSource): IExerciseImageRemoteDataSource

}