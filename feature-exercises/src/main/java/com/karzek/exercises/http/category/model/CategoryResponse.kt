package com.karzek.exercises.http.category.model

import com.google.gson.annotations.SerializedName
import com.karzek.exercises.domain.category.model.Category

data class CategoryResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) {
    fun toModel() = Category(id, name)
}

fun List<CategoryResponse>.toModels() = this.map { it.toModel() }

