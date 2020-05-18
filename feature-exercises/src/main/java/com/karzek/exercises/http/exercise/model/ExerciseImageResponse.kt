package com.karzek.exercises.http.exercise.model

import com.google.gson.annotations.SerializedName

data class ExerciseImageResponse(
    @SerializedName("image") val imageUrl: String
)