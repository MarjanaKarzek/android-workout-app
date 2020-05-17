package com.karzek.exercises.http.category.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)