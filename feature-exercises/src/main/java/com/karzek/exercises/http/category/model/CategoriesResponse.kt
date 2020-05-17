package com.karzek.exercises.http.category.model

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("results") val categories: List<CategoryResponse>
)