package com.karzek.exercises.database.exercise

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.karzek.exercises.database.category.CategoryEntity
import com.karzek.exercises.database.equipment.EquipmentEntity
import com.karzek.exercises.database.equipment.toModels
import com.karzek.exercises.database.muscle.MuscleEntity
import com.karzek.exercises.database.muscle.toModels
import com.karzek.exercises.database.relationship.ExerciseCategoryCrossRef
import com.karzek.exercises.database.relationship.ExerciseEquipmentCrossRef
import com.karzek.exercises.database.relationship.ExerciseMuscleCrossRef
import com.karzek.exercises.domain.exercise.model.Exercise

/**
 * For convenience the relationship to the category pretends to be n:m
 * like in this article found:
 *
 * https://spin.atomicobject.com/2019/04/01/android-room-tips/
 */
data class ExerciseEntityFull(
    @Embedded val exercise: ExerciseEntity,

    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "categoryId",
        associateBy = Junction(ExerciseCategoryCrossRef::class)
    )
    val _category: List<CategoryEntity>,

    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "muscleId",
        associateBy = Junction(ExerciseMuscleCrossRef::class)
    )
    val muscles: List<MuscleEntity>,

    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "equipmentId",
        associateBy = Junction(ExerciseEquipmentCrossRef::class)
    )
    val equipments: List<EquipmentEntity>
) {
    val category: CategoryEntity
        get() = _category.first()

    fun toModel() = Exercise(
        exercise.id,
        exercise.name,
        category.toModel(),
        exercise.description,
        exercise.imageThumbnailUrl,
        muscles.toModels(),
        equipments.toModels()
    )
}