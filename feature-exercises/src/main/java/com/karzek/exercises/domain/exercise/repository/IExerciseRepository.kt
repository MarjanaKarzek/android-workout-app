package com.karzek.exercises.domain.exercise.repository

import com.karzek.exercises.domain.exercise.model.Exercise
import io.reactivex.Single

interface IExerciseRepository {
    fun getExercises(
        currentPage: Int,
        pageSize: Int
    ): Single<Pair<List<Exercise>, Boolean>>

    fun getImageUrls(exerciseId: Int): Single<List<String>>
}
