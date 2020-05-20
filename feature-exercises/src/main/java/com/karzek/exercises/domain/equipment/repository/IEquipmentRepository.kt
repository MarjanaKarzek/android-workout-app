package com.karzek.exercises.domain.equipment.repository

import io.reactivex.Completable

interface IEquipmentRepository {
    fun validateCache(): Completable
}
