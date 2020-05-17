package com.karzek.exercises.domain.model

data class Exercise(
    val id: Int,
    val name: String,
    val category: String,
    val description: String,
    var imageThumbnailUrl: String? = null,
    val imagesUrls: List<String>? = null,
    val muscles: List<String>? = null,
    val equipment: List<String>? = null
)