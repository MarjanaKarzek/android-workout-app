package com.karzek.exercises.data.equipment

import com.karzek.exercises.data.equipment.contract.IEquipmentRemoteDataSource
import com.karzek.exercises.domain.equipment.model.Equipment
import com.karzek.exercises.http.equipment.IEquipmentApiService
import com.karzek.exercises.http.equipment.model.toModels
import io.reactivex.Single
import javax.inject.Inject

class EquipmentRemoteDataSource @Inject constructor(
    private val equipmentApiService: IEquipmentApiService
) : IEquipmentRemoteDataSource {

    override fun getAllEquipments(): Single<List<Equipment>> {
        return equipmentApiService.getEquipments()
            .map { it.equipments.toModels() }
    }

}
