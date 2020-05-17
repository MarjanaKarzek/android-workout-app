package com.karzek.exercises.http.muscle.model

import com.google.gson.annotations.SerializedName
import com.karzek.exercises.http.muscle.model.MuscleResponse

data class MusclesResponse(
    @SerializedName("results") val muscles: List<MuscleResponse>
)