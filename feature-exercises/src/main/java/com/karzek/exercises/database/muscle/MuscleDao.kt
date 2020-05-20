package com.karzek.exercises.database.muscle

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MuscleDao {

    @Query("SELECT COUNT(muscleId) FROM muscle_table")
    fun countItems(): Single<Int>

    @Query("SELECT * from muscle_table")
    fun getAll(): Single<List<MuscleEntity>>

    @Query("DELETE FROM muscle_table")
    fun deleteAll(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(muscles: List<MuscleEntity>)
}