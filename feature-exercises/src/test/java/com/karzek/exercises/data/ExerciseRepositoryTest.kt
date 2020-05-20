package com.karzek.exercises.data

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.exercise.PagedExerciseProvider
import com.karzek.exercises.data.exercise.contract.IExerciseRemoteDataSource
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.domain.exercise.repository.IPagedExerciseProvider
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

    private lateinit var providerPaged: IPagedExerciseProvider

    @BeforeEach
    override fun setup() {
        super.setup()
        providerPaged = PagedExerciseProvider(dataSource)
    }

    @Test
    fun `getExercises returns expected output`() {
        every { dataSource.getExercises(0, 1) } returns Single.just(Pair(exercises, false))

        providerPaged.getExercises(0, 1).test()
            .assertValue(Pair(exercises, false))
    }

    @Test
    fun `exceptions get passed to observer`() {
        every { dataSource.getExercises(0, 1) } returns Single.error(RuntimeException())

        providerPaged.getExercises(0, 1).test()
            .assertError(RuntimeException::class.java)
    }

}