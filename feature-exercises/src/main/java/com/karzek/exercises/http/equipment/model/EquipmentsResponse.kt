package com.karzek.exercises.http.equipment.model

import com.google.gson.annotations.SerializedName
import com.karzek.exercises.http.equipment.model.EquipmentResponse

data class EquipmentsResponse(
    @SerializedName("results") val equipments: List<EquipmentResponse>
)