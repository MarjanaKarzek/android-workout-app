package com.karzek.exercises.data.muscle

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.muscle.contract.IMuscleRemoteDataSource
import com.karzek.exercises.domain.muscle.model.Muscle
import com.karzek.exercises.http.muscle.IMuscleApiService
import com.karzek.exercises.http.muscle.model.MuscleResponse
import com.karzek.exercises.http.muscle.model.MusclesResponse
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MuscleRemoteDataSourceTest : BaseUnitTest() {

    private val muscleApiService: IMuscleApiService = mockk()

    private lateinit var dataSource: IMuscleRemoteDataSource

    private val muscleResponses = listOf(
        MuscleResponse(0, "Muscle 1"),
        MuscleResponse(1, "Muscle 2")
    )

    private val muscles = listOf(
        Muscle(0, "Muscle 1"),
        Muscle(1, "Muscle 2")
    )

    @BeforeEach
    override fun setup() {
        super.setup()
        dataSource = MuscleRemoteDataSource(muscleApiService)
    }

    @Test
    fun `getAllMuscles returns expected output`() {
        every { muscleApiService.getMuscles() } returns Single.just(MusclesResponse(muscleResponses))

        dataSource.getAllMuscles().test()
            .assertValue(muscles)
    }

    @Test
    fun `exceptions from api service get passed to observer`() {
        every { muscleApiService.getMuscles() } returns Single.error(RuntimeException())

        dataSource.getAllMuscles().test()
            .assertError(RuntimeException::class.java)
    }

}