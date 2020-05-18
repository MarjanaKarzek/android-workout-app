package com.karzek.exercises.data.exercise

import com.karzek.exercises.data.exercise.contract.IExerciseRemoteDataSource
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.domain.exercise.repository.IExerciseRepository
import io.reactivex.Single
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    private val exerciseRemoteDataSource: IExerciseRemoteDataSource
) : IExerciseRepository {
    override fun getExercises(
        currentPage: Int,
        pageSize: Int
    ): Single<Pair<List<Exercise>, Boolean>> {
        return exerciseRemoteDataSource.getExercises(currentPage, pageSize)
    }

    override fun getImageUrls(exerciseId: Int): Single<List<String>> {
        return exerciseRemoteDataSource.getImageUrls(exerciseId)
    }
}