package com.karzek.exercises.http.equipment.model

import com.google.gson.annotations.SerializedName
import com.karzek.exercises.domain.equipment.model.Equipment

data class EquipmentResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) {
    fun toModel() = Equipment(id, name)
}

fun List<EquipmentResponse>.toModels() = this.map { it.toModel() }