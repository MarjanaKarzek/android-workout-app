package com.karzek.exercises.domain.model

data class Exercise(
    val name: String,
    val category: String,
    val imageUrl: String? = null,
    val muscles: List<String>? = null,
    val equipment: List<String>? = null
)