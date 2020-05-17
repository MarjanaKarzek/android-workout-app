package com.karzek.exercises.http.exercise.model

import com.google.gson.annotations.SerializedName

data class ExerciseResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("category") val categoryId: Int,
    @SerializedName("description") val description: String,
    @SerializedName("muscles") val muscleIds: List<Int>,
    @SerializedName("equipment") val equipmentIds: List<Int>
)