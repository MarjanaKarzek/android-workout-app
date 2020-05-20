package com.karzek.exercises.domain.exercise.model

import android.os.Parcelable
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.domain.equipment.model.Equipment
import com.karzek.exercises.domain.muscle.model.Muscle
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Exercise(
    val id: Int,
    val name: String,
    val category: Category,
    val description: String?,
    var imageThumbnailUrl: String? = null,
    val muscles: List<Muscle>? = null,
    val equipment: List<Equipment>? = null
) : Parcelable