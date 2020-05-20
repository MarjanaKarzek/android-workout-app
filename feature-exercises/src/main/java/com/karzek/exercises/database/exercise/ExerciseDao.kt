package com.karzek.exercises.database.exercise

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ExerciseDao {

    @Query("SELECT COUNT(exerciseId) FROM exercise_table")
    fun countItems(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exercise: ExerciseEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(exercises: List<ExerciseEntity>): Completable

    @Transaction
    @Query("SELECT * FROM exercise_table ORDER BY name ASC")
    fun allExercises(): DataSource.Factory<Int, ExerciseEntityFull>

    @Query("DELETE FROM exercise_table")
    fun deleteAll(): Completable

}