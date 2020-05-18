package com.karzek.exercises.database.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.karzek.exercises.domain.category.model.Category

@Entity(tableName = "category_table")
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String
) {

    fun toModel() = Category(id, name)
}

fun List<CategoryEntity>.toModels() = this.map { it.toModel() }