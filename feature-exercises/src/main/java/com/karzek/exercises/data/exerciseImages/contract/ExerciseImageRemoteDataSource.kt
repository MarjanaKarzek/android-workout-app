package com.karzek.exercises.data.exerciseImages.contract

import com.karzek.exercises.http.exercise.IExerciseImagesApiService
import io.reactivex.Single
import javax.inject.Inject

class ExerciseImageRemoteDataSource @Inject constructor(
    private val exerciseImagesApiService: IExerciseImagesApiService
) : IExerciseImageRemoteDataSource {

    override fun getImageUrls(exerciseId: Int): Single<List<String>> {
        return exerciseImagesApiService.getExerciseImages(exerciseId)
            .map { response ->
                response.images.map { it.imageUrl }
            }
    }

}