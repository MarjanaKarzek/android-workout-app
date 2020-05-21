package com.karzek.exercises.database.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CategoryDao {

    @Query("SELECT COUNT(categoryId) FROM category_table")
    fun countItems(): Single<Int>

    @Query("SELECT * from category_table")
    fun getAll(): Single<List<CategoryEntity>>

    @Query("DELETE FROM category_table")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<CategoryEntity>)
}