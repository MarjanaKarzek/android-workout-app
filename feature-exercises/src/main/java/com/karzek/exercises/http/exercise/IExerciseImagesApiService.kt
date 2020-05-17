package com.karzek.exercises.http.exercise

import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface IExerciseImagesApiService {

    @GET("exerciseimage/{id}/thumbnails")
    fun getThumbnail(
        @Path("id") id: Int
    ): Single<ExerciseThumbnailResponse>

}