package com.karzek.exercises.data.exerciseImages

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.exerciseImages.contract.IExerciseImageRemoteDataSource
import com.karzek.exercises.domain.exerciseimage.repository.IExerciseImageRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ExerciseImageRepositoryTest : BaseUnitTest() {

    private val remoteDataSource: IExerciseImageRemoteDataSource = mockk(relaxed = true)

    private lateinit var repository: IExerciseImageRepository

    private val exerciseId = 0
    private val imageUrls = listOf("url1", "url2")
    private val emptyImageUrls = emptyList<String>()

    @BeforeEach
    override fun setup() {
        super.setup()
        repository = ExerciseImageRepository(remoteDataSource)
    }

    @Test
    fun `getImageUrls returns the expected output when there are urls`() {
        every { remoteDataSource.getImageUrls(exerciseId) } returns Single.just(imageUrls)

        repository.getImageUrls(exerciseId).test().assertValue(imageUrls)
    }

    @Test
    fun `getImageUrls returns the expected output when no urls were found`() {
        every { remoteDataSource.getImageUrls(exerciseId) } returns Single.just(emptyImageUrls)

        repository.getImageUrls(exerciseId).test().assertValue(emptyImageUrls)
    }

    @Test
    fun `exceptions from remote get passed to observer`() {
        every { remoteDataSource.getImageUrls(exerciseId) } returns Single.error(RuntimeException())

        repository.getImageUrls(exerciseId).test()
            .assertError(RuntimeException::class.java)
    }

}