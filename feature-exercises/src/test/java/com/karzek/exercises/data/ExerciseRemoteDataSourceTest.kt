package com.karzek.exercises.data

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.exercise.ExerciseRemoteDataSource
import com.karzek.exercises.data.exercise.contract.IExerciseRemoteDataSource
import com.karzek.exercises.data.exercise.model.ExerciseRaw
import com.karzek.exercises.http.exercise.IExerciseApiService
import com.karzek.exercises.http.exercise.IExerciseImagesApiService
import com.karzek.exercises.http.exercise.model.ExerciseResponse
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse.EmptyResponse
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse.ThumbnailResponse
import com.karzek.exercises.http.exercise.model.ExercisesResponse
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ExerciseRemoteDataSourceTest : BaseUnitTest() {

    private val exerciseApiService: IExerciseApiService = mockk()
    private val exerciseImagesApiService: IExerciseImagesApiService = mockk()

    private val exercisesResponse = ExercisesResponse(
        "nextPageUrl",
        listOf(
            ExerciseResponse(
                1,
                "name",
                0,
                "description",
                listOf(0),
                listOf(0)
            )
        )
    )

    private val thumbnailResponse = ThumbnailResponse("thumbnailUrl")
    private val emptyThumbnailResponse = EmptyResponse

    private val exercisesWithThumbnail = listOf(
        ExerciseRaw(
            1,
            "name",
            0,
            "description",
            "thumbnailUrl",
            listOf(0),
            listOf(0)
        )
    )

    private val exercisesWithoutThumbnail = listOf(
        ExerciseRaw(
            1,
            "name",
            0,
            "description",
            null,
            listOf(0),
            listOf(0)
        )
    )

    private lateinit var dataSource: IExerciseRemoteDataSource

    @BeforeEach
    override fun setup() {
        super.setup()
        dataSource = ExerciseRemoteDataSource(
            exerciseApiService,
            exerciseImagesApiService
        )
    }

    @Test
    fun `getExercises returns expected output with thumbnail url`() {
        every { exerciseApiService.getExercises(page = 1, limit = 1, category = null, query = null) } returns Single.just(exercisesResponse)
        every { exerciseImagesApiService.getThumbnail(1) } returns Single.just(thumbnailResponse)

        dataSource.getExercises(0, 1).test()
            .assertValue(Pair(exercisesWithThumbnail, false))
    }

    @Test
    fun `getExercises returns expected output without thumbnail url`() {
        every { exerciseApiService.getExercises(page = 1, limit = 1, category = null, query = null) } returns Single.just(exercisesResponse)
        every { exerciseImagesApiService.getThumbnail(1) } returns Single.just(emptyThumbnailResponse)

        dataSource.getExercises(0, 1).test()
            .assertValue(Pair(exercisesWithoutThumbnail, false))
    }

    @Test
    fun `exceptions from ExerciseApiService get passed to observer`() {
        every { exerciseApiService.getExercises(page = 1, limit = 1, category = null, query = null) } returns Single.error(RuntimeException())

        dataSource.getExercises(0, 1).test()
            .assertError(RuntimeException::class.java)
    }

    @Test
    fun `exceptions from ExerciseImagesApiService get passed to observer`() {
        every { exerciseApiService.getExercises(page = 1, limit = 1, category = null, query = null) } returns Single.just(exercisesResponse)
        every { exerciseImagesApiService.getThumbnail(1) } returns Single.error(RuntimeException())

        dataSource.getExercises(0, 1).test()
            .assertError(RuntimeException::class.java)
    }
}