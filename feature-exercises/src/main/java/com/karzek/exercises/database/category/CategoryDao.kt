package com.karzek.exercises.database.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CategoryDao {

    @Query("SELECT * from category_table")
    fun getAll(): Single<List<CategoryEntity>>

    @Query("DELETE FROM category_table")
    fun deleteAll(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(muscles: List<CategoryEntity>)
}