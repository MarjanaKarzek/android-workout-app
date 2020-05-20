package com.karzek.exercises.database.equipment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface EquipmentDao {

    @Query("SELECT COUNT(equipmentId) FROM equipment_table")
    fun countItems(): Single<Int>

    @Query("SELECT * from equipment_table")
    fun getAll(): Single<List<EquipmentEntity>>

    @Query("DELETE FROM equipment_table")
    fun deleteAll(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(equipments: List<EquipmentEntity>)
}