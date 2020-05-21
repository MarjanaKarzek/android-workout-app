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
    fun `validateCache completes without error when local data is valid`() {
        every { localDataSource.isCacheValid() } returns Single.just(true)

        repository.validateCache().test().assertComplete()

        verify(exactly = 1) { localDataSource.isCacheValid() }
        verify(exactly = 0) { remoteDataSource.getAllMuscles() }
        verify(exactly = 0) { localDataSource.setAllMuscles(any()) }
    }

    @Test
    fun `validateCache completes without error when local data is invalid and got updated from remote`() {
        every { localDataSource.isCacheValid() } returns Single.just(false)
        every { remoteDataSource.getAllMuscles() } returns Single.just(muscles)
        every { localDataSource.setAllMuscles(muscles) } returns Completable.complete()

        repository.validateCache().test().assertComplete()

        verify(exactly = 1) { localDataSource.isCacheValid() }
        verify(exactly = 1) { remoteDataSource.getAllMuscles() }
        verify(exactly = 1) { localDataSource.setAllMuscles(muscles) }
    }

    @Test
    fun `exceptions from local get passed to observer`() {
        every { localDataSource.isCacheValid() } returns Single.just(false)
        every { remoteDataSource.getAllMuscles() } returns Single.just(muscles)
        every { localDataSource.setAllMuscles(muscles) } returns Completable.error(RuntimeException())

        repository.validateCache().test()
            .assertError(RuntimeException::class.java)
    }

    @Test
    fun `exceptions from remote get passed to observer`() {
        every { localDataSource.isCacheValid() } returns Single.just(false)
        every { remoteDataSource.getAllMuscles() } returns Single.error(RuntimeException())

        repository.validateCache().test()
            .assertError(RuntimeException::class.java)
    }

}