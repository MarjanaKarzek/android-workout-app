package com.karzek.exercises.database.equipment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.karzek.exercises.domain.equipment.model.Equipment

@Entity(tableName = "equipment_table")
data class EquipmentEntity(
    @PrimaryKey
    @ColumnInfo(name = "equipmentId")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String
) {

    fun toModel() = Equipment(id, name)
}

fun List<EquipmentEntity>.toModels() = this.map { it.toModel() }