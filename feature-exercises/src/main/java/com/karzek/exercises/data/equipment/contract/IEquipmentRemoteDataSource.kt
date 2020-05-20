package com.karzek.exercises.data.equipment.contract

import com.karzek.exercises.domain.equipment.model.Equipment
import io.reactivex.Single

interface IEquipmentRemoteDataSource {
    fun getAllEquipments(): Single<List<Equipment>>
}
