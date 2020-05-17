package com.karzek.exercises.http.muscle

import com.karzek.exercises.http.muscle.model.MusclesResponse
import io.reactivex.Single
import retrofit2.http.GET

interface IMuscleApiService {
    @GET("muscle/")
    fun getMuscles(): Single<MusclesResponse>
}