package com.karzek.exercises.data.muscle

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.muscle.contract.IMuscleLocalDataSource
import com.karzek.exercises.data.muscle.contract.IMuscleRemoteDataSource
import com.karzek.exercises.domain.muscle.model.Muscle
import com.karzek.exercises.domain.muscle.repository.IMuscleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MuscleRepositoryTest : BaseUnitTest() {

    private val remoteDataSource: IMuscleRemoteDataSource = mockk(relaxed = true)
    private val localDataSource: IMuscleLocalDataSource = mockk(relaxed = true)

    private val muscles = listOf(
        Muscle(0, "Muscle 1"),
        Muscle(1, "Muscle 2")
    )

    private lateinit var repository: IMuscleRepository

    @BeforeEach
    override fun setup() {
        super.setup()
        repository = MuscleRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun `getAllMuscles returns expected output from cache if cache is valid`() {
        every { localDataSource.getAllMuscles() } returns Maybe.just(muscles)

        repository.getAllMuscles().test()
            .assertValue(muscles)

        verify(exactly = 0) { localDataSource.setAllMuscles(any()) }
    }

    @Test
    fun `getAllMuscles returns expected output from remote if cache is invalid`() {
        every { localDataSource.getAllMuscles() } returns Maybe.empty()
        every { localDataSource.setAllMuscles(muscles) } returns Completable.complete()
        every { remoteDataSource.getAllMuscles() } returns Single.just(muscles)

        repository.getAllMuscles().test()
            .assertValue(muscles)

        verify(exactly = 1) { localDataSource.setAllMuscles(muscles) }
    }

    @Test
    fun `exceptions from local get passed to observer`() {
        every { localDataSource.getAllMuscles() } returns Maybe.error(RuntimeException())

        repository.getAllMuscles().test()
            .assertError(RuntimeException::class.java)
    }

    @Test
    fun `exceptions from remote get passed to observer`() {
        every { localDataSource.getAllMuscles() } returns Maybe.empty()
        every { remoteDataSource.getAllMuscles() } returns Single.error(RuntimeException())

        repository.getAllMuscles().test()
            .assertError(RuntimeException::class.java)
    }

}