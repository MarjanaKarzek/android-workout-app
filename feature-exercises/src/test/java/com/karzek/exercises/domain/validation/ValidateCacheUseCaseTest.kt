package com.karzek.exercises.domain.validation

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.domain.category.repository.ICategoryRepository
import com.karzek.exercises.domain.equipment.repository.IEquipmentRepository
import com.karzek.exercises.domain.exercise.repository.IPagedExerciseProvider
import com.karzek.exercises.domain.muscle.repository.IMuscleRepository
import com.karzek.exercises.domain.validation.IValidateCacheUseCase.Input
import com.karzek.exercises.domain.validation.IValidateCacheUseCase.Output.ErrorUnknown
import com.karzek.exercises.domain.validation.IValidateCacheUseCase.Output.Success
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ValidateCacheUseCaseTest : BaseUnitTest() {

    private val categoryRepository: ICategoryRepository = mockk()
    private val muscleRepository: IMuscleRepository = mockk()
    private val equipmentRepository: IEquipmentRepository = mockk()
    private val exerciseProvider: IPagedExerciseProvider = mockk()

    private lateinit var useCase: IValidateCacheUseCase

    private val input = Input

    @BeforeEach
    override fun setup() {
        super.setup()
        useCase = ValidateCacheUseCase(categoryRepository, muscleRepository, equipmentRepository, exerciseProvider)
    }

    @Test
    fun `execute returns Success output when all Completables completed`() {
        every { categoryRepository.validateCache() } returns Completable.complete()
        every { muscleRepository.validateCache() } returns Completable.complete()
        every { equipmentRepository.validateCache() } returns Completable.complete()
        every { exerciseProvider.clearCache() } returns Completable.complete()

        useCase.execute(input).test().assertValue(Success)
    }

    @Test
    fun `exceptions from categoryRepository get mapped to ErrorUnknown output`() {
        every { categoryRepository.validateCache() } returns Completable.error(RuntimeException())
        every { muscleRepository.validateCache() } returns Completable.complete()
        every { equipmentRepository.validateCache() } returns Completable.complete()
        every { exerciseProvider.clearCache() } returns Completable.complete()

        useCase.execute(input).test().assertValue(ErrorUnknown)
    }

    @Test
    fun `exceptions from muscleRepository get mapped to ErrorUnknown output`() {
        every { categoryRepository.validateCache() } returns Completable.complete()
        every { muscleRepository.validateCache() } returns Completable.error(RuntimeException())
        every { equipmentRepository.validateCache() } returns Completable.complete()
        every { exerciseProvider.clearCache() } returns Completable.complete()

        useCase.execute(input).test().assertValue(ErrorUnknown)
    }

    @Test
    fun `exceptions from equipmentRepository get mapped to ErrorUnknown output`() {
        every { categoryRepository.validateCache() } returns Completable.complete()
        every { muscleRepository.validateCache() } returns Completable.complete()
        every { equipmentRepository.validateCache() } returns Completable.error(RuntimeException())
        every { exerciseProvider.clearCache() } returns Completable.complete()

        useCase.execute(input).test().assertValue(ErrorUnknown)
    }

    @Test
    fun `exceptions from exerciseProvider get mapped to ErrorUnknown output`() {
        every { categoryRepository.validateCache() } returns Completable.complete()
        every { muscleRepository.validateCache() } returns Completable.complete()
        every { equipmentRepository.validateCache() } returns Completable.complete()
        every { exerciseProvider.clearCache() } returns Completable.error(RuntimeException())

        useCase.execute(input).test().assertValue(ErrorUnknown)
    }
}