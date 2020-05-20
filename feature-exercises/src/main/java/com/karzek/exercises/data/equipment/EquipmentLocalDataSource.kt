package com.karzek.exercises.data.equipment

import android.content.SharedPreferences
import com.karzek.exercises.data.equipment.contract.IEquipmentLocalDataSource
import com.karzek.exercises.database.equipment.EquipmentDao
import com.karzek.exercises.database.equipment.EquipmentEntity
import com.karzek.exercises.database.equipment.toModels
import com.karzek.exercises.domain.equipment.model.Equipment
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

class EquipmentLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val equipmentDao: EquipmentDao
) : IEquipmentLocalDataSource {

    override fun getAllEquipments(): Maybe<List<Equipment>> {
        return isCacheValid()
            .flatMapMaybe {
                if (it) {
                    equipmentDao.getAll()
                        .map { entities ->
                            entities.toModels()
                        }
                        .toMaybe()
                } else {
                    Maybe.empty()
                }
            }
    }

    override fun setAllEquipments(equipments: List<Equipment>): Completable {
        return Completable.fromCallable {
            equipmentDao.deleteAll()
            val entities = equipments.map {
                EquipmentEntity(it.id, it.name)
            }
            equipmentDao.insertAll(entities)
            sharedPreferences.edit().putLong(EQUIPMENT_CACHE_TIME_STAMP, Date().time).commit()
        }
    }

    override fun isCacheValid(): Single<Boolean> {
        return equipmentDao.countItems()
            .map {
                if (it > 0) {
                    val currentTime = Date().time
                    val timestamp = sharedPreferences.getLong(EQUIPMENT_CACHE_TIME_STAMP, 0)
                    (timestamp + ONE_DAY) > currentTime
                } else {
                    false
                }
            }
    }

    companion object {
        private const val EQUIPMENT_CACHE_TIME_STAMP = "EQUIPMENT_CACHE_TIME_STAMP"
        private const val ONE_DAY = 24 * 60 * 60 * 1000L
    }

}
