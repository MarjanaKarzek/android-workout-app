package com.karzek.exercises.http.equipment.model

import com.google.gson.annotations.SerializedName

data class EquipmentResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)