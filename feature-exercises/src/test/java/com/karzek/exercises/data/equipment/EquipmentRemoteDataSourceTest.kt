package com.karzek.exercises.data.equipment

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.equipment.contract.IEquipmentRemoteDataSource
import com.karzek.exercises.domain.equipment.model.Equipment
import com.karzek.exercises.http.equipment.IEquipmentApiService
import com.karzek.exercises.http.equipment.model.EquipmentResponse
import com.karzek.exercises.http.equipment.model.EquipmentsResponse
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EquipmentRemoteDataSourceTest : BaseUnitTest() {

    private val equipmentApiService: IEquipmentApiService = mockk()

    private lateinit var dataSource: IEquipmentRemoteDataSource

    private val equipmentsResponses = listOf(
        EquipmentResponse(0, "Equipment 1"),
        EquipmentResponse(1, "Equipment 2")
    )

    private val equipments = listOf(
        Equipment(0, "Equipment 1"),
        Equipment(1, "Equipment 2")
    )

    @BeforeEach
    override fun setup() {
        super.setup()
        dataSource = EquipmentRemoteDataSource(equipmentApiService)
    }

    @Test
    fun `getAllEquipments returns expected output`() {
        every { equipmentApiService.getEquipments() } returns Single.just(EquipmentsResponse(equipmentsResponses))

        dataSource.getAllEquipments().test()
            .assertValue(equipments)
    }

    @Test
    fun `exceptions from api service get passed to observer`() {
        every { equipmentApiService.getEquipments() } returns Single.error(RuntimeException())

        dataSource.getAllEquipments().test()
            .assertError(RuntimeException::class.java)
    }

}