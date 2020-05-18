package com.karzek.exercises.data.exercise.contract

import com.karzek.exercises.domain.exercise.model.Exercise
import io.reactivex.Single

interface IExerciseRemoteDataSource {
    fun getExercises(
        currentPage: Int,
        pageSize: Int
    ): Single<Pair<List<Exercise>, Boolean>>

    fun getImageUrls(exerciseId: Int): Single<List<String>>
}
