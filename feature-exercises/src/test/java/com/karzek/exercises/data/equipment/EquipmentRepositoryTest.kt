package com.karzek.exercises.data.equipment

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.equipment.contract.IEquipmentLocalDataSource
import com.karzek.exercises.data.equipment.contract.IEquipmentRemoteDataSource
import com.karzek.exercises.domain.equipment.model.Equipment
import com.karzek.exercises.domain.equipment.repository.IEquipmentRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EquipmentRepositoryTest : BaseUnitTest() {

    private val remoteDataSource: IEquipmentRemoteDataSource = mockk(relaxed = true)
    private val localDataSource: IEquipmentLocalDataSource = mockk(relaxed = true)

    private val equipments = listOf(
        Equipment(0, "Equipment 1"),
        Equipment(1, "Equipment 2")
    )

    private lateinit var repository: IEquipmentRepository

    @BeforeEach
    override fun setup() {
        super.setup()
        repository = EquipmentRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun `validateCache completes without error when local data is valid`() {
        every { localDataSource.isCacheValid() } returns Single.just(true)

        repository.validateCache().test().assertComplete()

        verify(exactly = 1) { localDataSource.isCacheValid() }
        verify(exactly = 0) { remoteDataSource.getAllEquipments() }
        verify(exactly = 0) { localDataSource.setAllEquipments(any()) }
    }

    @Test
    fun `validateCache completes without error when local data is invalid and got updated from remote`() {
        every { localDataSource.isCacheValid() } returns Single.just(false)
        every { remoteDataSource.getAllEquipments() } returns Single.just(equipments)
        every { localDataSource.setAllEquipments(equipments) } returns Completable.complete()

        repository.validateCache().test().assertComplete()

        verify(exactly = 1) { localDataSource.isCacheValid() }
        verify(exactly = 1) { remoteDataSource.getAllEquipments() }
        verify(exactly = 1) { localDataSource.setAllEquipments(equipments) }
    }

    @Test
    fun `exceptions from local get passed to observer`() {
        every { localDataSource.isCacheValid() } returns Single.just(false)
        every { remoteDataSource.getAllEquipments() } returns Single.just(equipments)
        every { localDataSource.setAllEquipments(equipments) } returns Completable.error(RuntimeException())

        repository.validateCache().test()
            .assertError(RuntimeException::class.java)
    }

    @Test
    fun `exceptions from remote get passed to observer`() {
        every { localDataSource.isCacheValid() } returns Single.just(false)
        every { remoteDataSource.getAllEquipments() } returns Single.error(RuntimeException())

        repository.validateCache().test()
            .assertError(RuntimeException::class.java)
    }

}