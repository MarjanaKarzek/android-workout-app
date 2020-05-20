package com.karzek.exercises.data.equipment.contract

import com.karzek.exercises.domain.equipment.model.Equipment
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface IEquipmentLocalDataSource {
    fun getAllEquipments(): Maybe<List<Equipment>>
    fun setAllEquipments(equipments: List<Equipment>): Completable
    fun isCacheValid(): Single<Boolean>
}
