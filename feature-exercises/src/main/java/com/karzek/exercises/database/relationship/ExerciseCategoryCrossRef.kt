package com.karzek.exercises.database.relationship

import androidx.room.Entity

@Entity(
    tableName = "exercise_category_relation_table",
    primaryKeys = ["exerciseId", "categoryId"]
)
data class ExerciseCategoryCrossRef(
    val exerciseId: Long,
    val categoryId: Long
)