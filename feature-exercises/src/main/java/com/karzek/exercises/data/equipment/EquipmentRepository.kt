package com.karzek.exercises.data.equipment

import com.karzek.exercises.data.equipment.contract.IEquipmentLocalDataSource
import com.karzek.exercises.data.equipment.contract.IEquipmentRemoteDataSource
import com.karzek.exercises.domain.equipment.repository.IEquipmentRepository
import io.reactivex.Completable
import javax.inject.Inject

class EquipmentRepository @Inject constructor(
    private val equipmentRemoteDataSource: IEquipmentRemoteDataSource,
    private val equipmentLocalDataSource: IEquipmentLocalDataSource
) : IEquipmentRepository {

    override fun validateCache(): Completable {
        return equipmentLocalDataSource.isCacheValid()
            .flatMapCompletable { isValid ->
                if (isValid) {
                    Completable.complete()
                } else {
                    updateCache()
                }
            }
    }

    private fun updateCache(): Completable {
        return equipmentRemoteDataSource.getAllEquipments()
            .flatMapCompletable {
                equipmentLocalDataSource.setAllEquipments(it)
            }
    }

}