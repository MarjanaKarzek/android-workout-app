package com.karzek.exercises.di

import com.karzek.exercises.data.equipment.EquipmentLocalDataSource
import com.karzek.exercises.data.equipment.EquipmentRemoteDataSource
import com.karzek.exercises.data.equipment.EquipmentRepository
import com.karzek.exercises.data.equipment.contract.IEquipmentLocalDataSource
import com.karzek.exercises.data.equipment.contract.IEquipmentRemoteDataSource
import com.karzek.exercises.domain.equipment.repository.IEquipmentRepository
import dagger.Binds
import dagger.Module

@Module
interface EquipmentModule {

    @Binds
    fun bindEquipmentRepository(equipmentRepository: EquipmentRepository): IEquipmentRepository

    @Binds
    fun bindEquipmentRemoteDataSource(equipmentRemoteDataSource: EquipmentRemoteDataSource): IEquipmentRemoteDataSource

    @Binds
    fun bindEquipmentLocalDataSource(equipmentLocalDataSource: EquipmentLocalDataSource): IEquipmentLocalDataSource

}