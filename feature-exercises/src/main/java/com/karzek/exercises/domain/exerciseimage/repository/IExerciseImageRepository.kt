package com.karzek.exercises.domain.exerciseimage.repository

import io.reactivex.Single

interface IExerciseImageRepository {
    fun getImageUrls(exerciseId: Int): Single<List<String>>
}
