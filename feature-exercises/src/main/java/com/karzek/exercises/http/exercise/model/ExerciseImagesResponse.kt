package com.karzek.exercises.http.exercise.model

import com.google.gson.annotations.SerializedName
import com.karzek.exercises.http.exercise.model.ExerciseResponse

data class ExerciseImagesResponse(
    @SerializedName("results") val images: List<ExerciseImageResponse>
)