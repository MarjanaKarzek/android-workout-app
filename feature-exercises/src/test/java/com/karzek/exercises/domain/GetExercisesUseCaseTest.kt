package com.karzek.exercises.domain

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.domain.IGetExercisesUseCase.Input
import com.karzek.exercises.domain.IGetExercisesUseCase.Output.ErrorUnknown
import com.karzek.exercises.domain.IGetExercisesUseCase.Output.Success
import com.karzek.exercises.domain.model.Exercise
import com.karzek.exercises.domain.repository.IExerciseRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetExercisesUseCaseTest : BaseUnitTest() {

    private val repository: IExerciseRepository = mockk()

    private val input = Input(0, 1)
    private val exercises = listOf(
        Exercise(
            1,
            "name",
            "category",
            "description"
        )
    )

    private lateinit var useCase: IGetExercisesUseCase

    @BeforeEach
    override fun setup() {
        super.setup()
        useCase = GetExercisesUseCase(repository)
    }

    @Test
    fun `successful execution returns Success output`() {
        every { repository.getExercises(0, 1) } returns Single.just(Pair(exercises, false))

        useCase.execute(input).test()
            .assertValue(Success(exercises, false))
    }

    @Test
    fun `exceptions get mapped to ErrorUnknown output`() {
        every { repository.getExercises(0, 1) } returns Single.error(RuntimeException())

        useCase.execute(input).test()
            .assertValue(ErrorUnknown)
    }

}