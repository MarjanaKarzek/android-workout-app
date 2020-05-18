package com.karzek.exercises.database.muscle

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.karzek.exercises.domain.muscle.model.Muscle

@Entity(tableName = "muscle_table")
data class MuscleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String
) {

    fun toModel() = Muscle(id, name)
}

fun List<MuscleEntity>.toModels() = this.map { it.toModel() }