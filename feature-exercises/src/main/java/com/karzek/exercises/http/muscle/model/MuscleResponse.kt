package com.karzek.exercises.http.muscle.model

import com.google.gson.annotations.SerializedName
import com.karzek.exercises.domain.muscle.model.Muscle

data class MuscleResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) {
    fun toModel() = Muscle(id, name)
}

fun List<MuscleResponse>.toModels() = this.map { it.toModel() }