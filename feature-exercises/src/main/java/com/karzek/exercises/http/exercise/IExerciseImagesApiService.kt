package com.karzek.exercises.http.exercise

import com.karzek.exercises.http.exercise.model.ExerciseImagesResponse
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IExerciseImagesApiService {

    @GET("exerciseimage/{id}/thumbnails")
    fun getThumbnail(
        @Path("id") id: Int
    ): Single<ExerciseThumbnailResponse>

    @GET("exerciseimage/")
    fun getExerciseImages(
        @Query("exercise") exerciseId: Int,
        @Query("status") status: Int? = 2
    ): Single<ExerciseImagesResponse>

}