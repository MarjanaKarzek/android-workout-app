package com.karzek.exercises.database.relationship

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable

@Dao
interface ExerciseRelationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRelatedCategories(exerciseCategories: List<ExerciseCategoryCrossRef>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRelatedMuscles(exerciseMuscles: List<ExerciseMuscleCrossRef>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRelatedEquipments(exerciseEquipments: List<ExerciseEquipmentCrossRef>): Completable

    @Query("DELETE FROM exercise_equipment_relation_table")
    fun deleteAllEquipmentRelations(): Completable

    @Query("DELETE FROM exercise_muscle_relation_table")
    fun deleteAllMuscleRelations(): Completable

    @Query("DELETE FROM exercise_category_relation_table")
    fun deleteAllCategoryRelations(): Completable
}