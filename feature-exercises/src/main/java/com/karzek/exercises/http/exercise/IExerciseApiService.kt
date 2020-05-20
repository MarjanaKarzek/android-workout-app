package com.karzek.exercises.http.exercise

import com.karzek.exercises.http.exercise.model.ExercisesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface IExerciseApiService {

    @GET("exercise/")
    fun getExercises(
        @Query("language") language: Int? = 2,
        @Query("status") status: Int? = 2,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("ordering") ordering: String? = "name",
        @Query("name") query: String?,
        @Query("category") category: Int?
    ): Single<ExercisesResponse>

}