package com.karzek.exercises.http.exercise.model

import com.google.gson.annotations.SerializedName
import com.karzek.exercises.http.exercise.model.ExerciseResponse

data class ExercisesResponse(
    @SerializedName("next") val nextPage: String?,
    @SerializedName("results") val exercises: List<ExerciseResponse>
)