package com.karzek.exercises.domain.repository

import com.karzek.exercises.domain.model.Exercise
import io.reactivex.Single

interface IExerciseRepository {
    fun getExercises(
        currentPage: Int,
        pageSize: Int
    ): Single<Pair<List<Exercise>, Boolean>>
}
