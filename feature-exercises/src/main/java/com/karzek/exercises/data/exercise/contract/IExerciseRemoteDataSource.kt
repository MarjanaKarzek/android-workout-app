package com.karzek.exercises.data.exercise.contract

import com.karzek.exercises.data.exercise.model.ExerciseRaw
import io.reactivex.Completable
import io.reactivex.Single

interface IExerciseRemoteDataSource {
    fun getExercises(
        currentPage: Int,
        pageSize: Int
    ): Single<Pair<List<ExerciseRaw>, Boolean>>

    fun setQueryFilter(queryFilter: String?): Completable
    fun setCategoryFilter(id: Int?): Completable
}
