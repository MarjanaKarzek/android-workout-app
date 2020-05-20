package com.karzek.exercises.domain.muscle.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Muscle(
    val id: Int,
    val name: String
) : Parcelable {
    override fun toString() = name
}