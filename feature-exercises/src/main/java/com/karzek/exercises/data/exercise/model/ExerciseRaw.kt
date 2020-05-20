package com.karzek.exercises.data.exercise.model

data class ExerciseRaw(
    val id: Int,
    val name: String,
    val categoryId: Int,
    val description: String?,
    var imageThumbnailUrl: String? = null,
    val muscleIds: List<Int>? = null,
    val equipmentIds: List<Int>? = null
)