package com.karzek.exercises.di

import com.karzek.exercises.data.muscle.MuscleLocalDataSource
import com.karzek.exercises.data.muscle.MuscleRemoteDataSource
import com.karzek.exercises.data.muscle.MuscleRepository
import com.karzek.exercises.data.muscle.contract.IMuscleLocalDataSource
import com.karzek.exercises.data.muscle.contract.IMuscleRemoteDataSource
import com.karzek.exercises.domain.muscle.repository.IMuscleRepository
import dagger.Binds
import dagger.Module

@Module
interface MuscleModule {

    @Binds
    fun bindMuscleRepository(muscleRepository: MuscleRepository): IMuscleRepository

    @Binds
    fun bindMuscleRemoteDataSource(muscleRemoteDataSource: MuscleRemoteDataSource): IMuscleRemoteDataSource

    @Binds
    fun bindMuscleLocalDataSource(muscleLocalDataSource: MuscleLocalDataSource): IMuscleLocalDataSource

}