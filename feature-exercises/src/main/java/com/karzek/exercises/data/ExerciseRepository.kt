package com.karzek.exercises.data

import com.karzek.exercises.domain.model.Exercise
import com.karzek.exercises.domain.repository.IExerciseRepository
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
}