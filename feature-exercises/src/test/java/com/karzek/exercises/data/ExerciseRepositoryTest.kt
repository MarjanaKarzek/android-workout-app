package com.karzek.exercises.data

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.domain.model.Exercise
import com.karzek.exercises.domain.repository.IExerciseRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ExerciseRepositoryTest : BaseUnitTest() {

    private val dataSource: IExerciseRemoteDataSource = mockk()

    private val exercises = listOf(
        Exercise(
            1,
            "name",
            "category",
            "description"
        )
    )

    private lateinit var repository: IExerciseRepository

    @BeforeEach
    override fun setup() {
        super.setup()
        repository = ExerciseRepository(dataSource)
    }

    @Test
    fun `getExercises returns expected output`() {
        every { dataSource.getExercises(0, 1) } returns Single.just(Pair(exercises, false))

        repository.getExercises(0, 1).test()
            .assertValue(Pair(exercises, false))
    }

    @Test
    fun `exceptions get passed to observer`() {
        every { dataSource.getExercises(0, 1) } returns Single.error(RuntimeException())

        repository.getExercises(0, 1).test()
            .assertError(RuntimeException::class.java)
    }

}