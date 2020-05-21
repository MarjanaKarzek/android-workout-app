package com.karzek.exercises.data.equipment

import android.content.SharedPreferences
import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.equipment.contract.IEquipmentLocalDataSource
import com.karzek.exercises.database.equipment.EquipmentDao
import com.karzek.exercises.database.equipment.EquipmentEntity
import com.karzek.exercises.domain.equipment.model.Equipment
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

internal class EquipmentLocalDataSourceTest : BaseUnitTest() {

    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)
    private val equipmentDao: EquipmentDao = mockk(relaxed = true)

    private lateinit var dataSource: IEquipmentLocalDataSource

    private val equipmentEntities = listOf(
        EquipmentEntity(0, "Equipment 1"),
        EquipmentEntity(1, "Equipment 2")
    )

    private val equipments = listOf(
        Equipment(0, "Equipment 1"),
        Equipment(1, "Equipment 2")
    )

    @BeforeEach
    override fun setup() {
        super.setup()
        dataSource = EquipmentLocalDataSource(sharedPreferences, equipmentDao)
    }

    @Test
    fun `isCacheValid returns true when cache is valid`() {
        every { equipmentDao.countItems() } returns Single.just(1)

        val currentTime = Date().time
        every { sharedPreferences.getLong("EQUIPMENT_CACHE_TIME_STAMP", any()) } returns currentTime

        dataSource.isCacheValid().test()
            .assertValue(true)
    }

    @Test
    fun `isCacheValid returns false when cache is invalid due to expired time stamp`() {
        every { equipmentDao.countItems() } returns Single.just(1)

        every { sharedPreferences.getLong("EQUIPMENT_CACHE_TIME_STAMP", any()) } returns 0

        dataSource.isCacheValid().test()
            .assertValue(false)
    }

    @Test
    fun `isCacheValid returns false when cache is invalid due to empty table`() {
        every { equipmentDao.countItems() } returns Single.just(0)

        dataSource.isCacheValid().test()
            .assertValue(false)
    }

    @Test
    fun `setAllEquipments removes the old data sets it to the new data and updates the timestamp`() {
        dataSource.setAllEquipments(equipments).test()
            .assertComplete()

        verify(exactly = 1) { equipmentDao.deleteAll() }
        verify(exactly = 1) { equipmentDao.insertAll(equipmentEntities) }
        verify(exactly = 1) { sharedPreferences.edit().putLong("EQUIPMENT_CACHE_TIME_STAMP", any()) }
    }

    @Test
    fun `exceptions from dao get passed to observer`() {
        val currentTime = Date().time
        every { sharedPreferences.getLong("EQUIPMENT_CACHE_TIME_STAMP", any()) } returns currentTime
        every { equipmentDao.getAll() } returns Single.error(RuntimeException())

        dataSource.isCacheValid().test()
            .assertError(RuntimeException::class.java)
    }
}