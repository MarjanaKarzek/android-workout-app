package com.karzek.exercises.data.exerciseImages

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.exerciseImages.contract.IExerciseImageRemoteDataSource
import com.karzek.exercises.http.exercise.IExerciseImagesApiService
import com.karzek.exercises.http.exercise.model.ExerciseImageResponse
import com.karzek.exercises.http.exercise.model.ExerciseImagesResponse
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ExerciseImageRemoteDataSourceTest : BaseUnitTest() {

    private val exerciseImagesApiService: IExerciseImagesApiService = mockk()

    private lateinit var dataSource: IExerciseImageRemoteDataSource

    private val imageResponses = listOf(
        ExerciseImageResponse("url1"),
        ExerciseImageResponse("url2")
    )

    private val imageUrls = listOf("url1", "url2")

    private val exerciseId = 0

    @BeforeEach
    override fun setup() {
        super.setup()
        dataSource = ExerciseImageRemoteDataSource(exerciseImagesApiService)
    }

    @Test
    fun `getAllMuscles returns expected output`() {
        every { exerciseImagesApiService.getExerciseImages(exerciseId) } returns Single.just(ExerciseImagesResponse(imageResponses))

        dataSource.getImageUrls(exerciseId).test()
            .assertValue(imageUrls)
    }

    @Test
    fun `exceptions from api service get passed to observer`() {
        every { exerciseImagesApiService.getExerciseImages(exerciseId) } returns Single.error(RuntimeException())

        dataSource.getImageUrls(exerciseId).test()
            .assertError(RuntimeException::class.java)
    }
}