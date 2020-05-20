package com.karzek.exercises.database.relationship

import androidx.room.Entity

@Entity(
    tableName = "exercise_equipment_relation_table",
    primaryKeys = ["exerciseId", "equipmentId"]
)
data class ExerciseEquipmentCrossRef(
    val exerciseId: Long,
    val equipmentId: Long
)