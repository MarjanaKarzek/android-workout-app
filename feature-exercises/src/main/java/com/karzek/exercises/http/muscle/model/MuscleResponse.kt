package com.karzek.exercises.http.muscle.model

import com.google.gson.annotations.SerializedName

data class MuscleResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)