package com.karzek.exercises.data.exerciseImages.contract

import io.reactivex.Single

interface IExerciseImageRemoteDataSource {
    fun getImageUrls(exerciseId: Int): Single<List<String>>
}