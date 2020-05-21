package com.karzek.exercises.domain.exerciseimage

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Input
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Output.ErrorUnknown
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Output.Success
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Output.SuccessNoData
import com.karzek.exercises.domain.exerciseimage.repository.IExerciseImageRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetImagesForExerciseUseCaseTest : BaseUnitTest() {

    private val exerciseImageRepository: IExerciseImageRepository = mockk()

    private lateinit var useCase: IGetImagesForExerciseUseCase

    private val exerciseId = 0

    private val input = Input(exerciseId)

    private val imageUrls = listOf("url1")

    @BeforeEach
    override fun setup() {
        super.setup()
        useCase = GetImagesForExerciseUseCase(exerciseImageRepository)
    }

    @Test
    fun `execute returns Success output with list of images on successful execution`() {
        every { exerciseImageRepository.getImageUrls(exerciseId) } returns Single.just(imageUrls)

        useCase.execute(input).test().assertValue(Success(imageUrls))
    }

    @Test
    fun `execute returns SuccessNoData output on successful execution with empty list`() {
        every { exerciseImageRepository.getImageUrls(exerciseId) } returns Single.just(emptyList())

        useCase.execute(input).test().assertValue(SuccessNoData)
    }

    @Test
    fun `exceptions from repository get mapped to ErrorUnknown output`() {
        every { exerciseImageRepository.getImageUrls(exerciseId) } returns Single.error(RuntimeException())

        useCase.execute(input).test().assertValue(ErrorUnknown)
    }
}