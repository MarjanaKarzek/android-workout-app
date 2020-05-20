package com.karzek.exercises.http.exercise.model

import com.google.gson.annotations.SerializedName
import com.karzek.exercises.data.exercise.model.ExerciseRaw

data class ExerciseResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("category") val categoryId: Int,
    @SerializedName("description") val description: String?,
    @SerializedName("muscles") val muscleIds: List<Int>,
    @SerializedName("equipment") val equipmentIds: List<Int>
) {
    fun toModel() = ExerciseRaw(id, name, categoryId, description, null, muscleIds, equipmentIds)
}

fun List<ExerciseResponse>.toModels() = this.map { it.toModel() }