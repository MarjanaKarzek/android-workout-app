package com.karzek.exercises.database.relationship

import androidx.room.Entity

@Entity(
    tableName = "exercise_muscle_relation_table",
    primaryKeys = ["exerciseId", "muscleId"]
)
data class ExerciseMuscleCrossRef(
    val exerciseId: Long,
    val muscleId: Long
)