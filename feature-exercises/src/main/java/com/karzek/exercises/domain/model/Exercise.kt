package com.karzek.exercises.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Exercise(
    val id: Int,
    val name: String,
    val category: String,
    val description: String?,
    var imageThumbnailUrl: String? = null,
    val muscles: List<String>? = null,
    val equipment: List<String>? = null
) : Parcelable