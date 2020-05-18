package com.karzek.exercises.data

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.exercise.ExerciseRemoteDataSource
import com.karzek.exercises.data.exercise.contract.IExerciseRemoteDataSource
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.http.category.ICategoryApiService
import com.karzek.exercises.http.category.model.CategoriesResponse
import com.karzek.exercises.http.category.model.CategoryResponse
import com.karzek.exercises.http.equipment.IEquipmentApiService
import com.karzek.exercises.http.equipment.model.EquipmentResponse
import com.karzek.exercises.http.equipment.model.EquipmentsResponse
import com.karzek.exercises.http.exercise.IExerciseApiService
import com.karzek.exercises.http.exercise.IExerciseImagesApiService
import com.karzek.exercises.http.exercise.model.ExerciseResponse
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse.EmptyResponse
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse.ThumbnailResponse
import com.karzek.exercises.http.exercise.model.ExercisesResponse
import com.karzek.exercises.http.muscle.IMuscleApiService
import com.karzek.exercises.http.muscle.model.MuscleResponse
import com.karzek.exercises.http.muscle.model.MusclesResponse
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ExerciseRemoteDataSourceTest : BaseUnitTest() {

    private val exerciseApiService: IExerciseApiService = mockk()
    private val exerciseImagesApiService: IExerciseImagesApiService = mockk()
    private val muscleApiService: IMuscleApiService = mockk()
    private val equipmentApiService: IEquipmentApiService = mockk()
    private val categoryApiService: ICategoryApiService = mockk()

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
    private val musclesResponse = MusclesResponse(listOf(MuscleResponse(0, "muscle")))
    private val categoriesResponse = CategoriesResponse(listOf(CategoryResponse(0, "category")))
    private val equipmentResponse = EquipmentsResponse(listOf(EquipmentResponse(0, "equipment")))

    private val exercisesWithThumbnail = listOf(
        Exercise(
            1,
            "name",
            "category",
            "description",
            "thumbnailUrl",
            listOf("muscle"),
            listOf("equipment")
        )
    )

    private val exercisesWithoutThumbnail = listOf(
        Exercise(
            1,
            "name",
            "category",
            "description",
            null,
            listOf("muscle"),
            listOf("equipment")
        )
    )

    private lateinit var dataSource: IExerciseRemoteDataSource

    @BeforeEach
    override fun setup() {
        super.setup()
        dataSource = ExerciseRemoteDataSource(
            exerciseApiService,
            exerciseImagesApiService,
            muscleApiService,
            equipmentApiService,
            categoryApiService
        )
    }

    @Test
    fun `getExercises returns expected output with thumbnail url`() {
        every { exerciseApiService.getExercises(2, 2, 1, 1) } returns Single.just(exercisesResponse)
        every { exerciseImagesApiService.getThumbnail(1) } returns Single.just(thumbnailResponse)
        every { muscleApiService.getMuscles() } returns Single.just(musclesResponse)
        every { categoryApiService.getCategories() } returns Single.just(categoriesResponse)
        every { equipmentApiService.getEquipments() } returns Single.just(equipmentResponse)

        dataSource.getExercises(0, 1).test()
            .assertValue(Pair(exercisesWithThumbnail, false))
    }

    @Test
    fun `getExercises returns expected output without thumbnail url`() {
        every { exerciseApiService.getExercises(2, 2, 1, 1) } returns Single.just(exercisesResponse)
        every { exerciseImagesApiService.getThumbnail(1) } returns Single.just(emptyThumbnailResponse)
        every { muscleApiService.getMuscles() } returns Single.just(musclesResponse)
        every { categoryApiService.getCategories() } returns Single.just(categoriesResponse)
        every { equipmentApiService.getEquipments() } returns Single.just(equipmentResponse)

        dataSource.getExercises(0, 1).test()
            .assertValue(Pair(exercisesWithoutThumbnail, false))
    }

    @Test
    fun `exceptions from ExerciseApiService get passed to observer`() {
        every { muscleApiService.getMuscles() } returns Single.just(musclesResponse)
        every { categoryApiService.getCategories() } returns Single.just(categoriesResponse)
        every { equipmentApiService.getEquipments() } returns Single.just(equipmentResponse)
        every { exerciseApiService.getExercises(2, 2, 1, 1) } returns Single.error(RuntimeException())

        dataSource.getExercises(0, 1).test()
            .assertError(RuntimeException::class.java)
    }

    @Test
    fun `exceptions from ExerciseImagesApiService get passed to observer`() {
        every { exerciseApiService.getExercises(2, 2, 1, 1) } returns Single.just(exercisesResponse)
        every { muscleApiService.getMuscles() } returns Single.just(musclesResponse)
        every { categoryApiService.getCategories() } returns Single.just(categoriesResponse)
        every { equipmentApiService.getEquipments() } returns Single.just(equipmentResponse)
        every { exerciseImagesApiService.getThumbnail(1) } returns Single.error(RuntimeException())

        dataSource.getExercises(0, 1).test()
            .assertError(RuntimeException::class.java)
    }

    @Test
    fun `exceptions from MuscleApiService get passed to observer`() {
        every { exerciseApiService.getExercises(2, 2, 1, 1) } returns Single.just(exercisesResponse)
        every { categoryApiService.getCategories() } returns Single.just(categoriesResponse)
        every { equipmentApiService.getEquipments() } returns Single.just(equipmentResponse)
        every { muscleApiService.getMuscles() } returns Single.error(RuntimeException())

        dataSource.getExercises(0, 1).test()
            .assertError(RuntimeException::class.java)
    }

    @Test
    fun `exceptions from CategoryApiService get passed to observer`() {
        every { exerciseApiService.getExercises(2, 2, 1, 1) } returns Single.just(exercisesResponse)
        every { muscleApiService.getMuscles() } returns Single.just(musclesResponse)
        every { equipmentApiService.getEquipments() } returns Single.just(equipmentResponse)
        every { categoryApiService.getCategories() } returns Single.error(RuntimeException())

        dataSource.getExercises(0, 1).test()
            .assertError(RuntimeException::class.java)
    }

    @Test
    fun `exceptions from EquipmentApiService get passed to observer`() {
        every { exerciseApiService.getExercises(2, 2, 1, 1) } returns Single.just(exercisesResponse)
        every { muscleApiService.getMuscles() } returns Single.just(musclesResponse)
        every { categoryApiService.getCategories() } returns Single.just(categoriesResponse)
        every { equipmentApiService.getEquipments() } returns Single.error(RuntimeException())

        dataSource.getExercises(0, 1).test()
            .assertError(RuntimeException::class.java)
    }

    @Test
    fun `exceptions from EquipmentImagesApiService get passed to observer`() {
        every { exerciseApiService.getExercises(2, 2, 1, 1) } returns Single.just(exercisesResponse)
        every { muscleApiService.getMuscles() } returns Single.just(musclesResponse)
        every { categoryApiService.getCategories() } returns Single.just(categoriesResponse)
        every { equipmentApiService.getEquipments() } returns Single.just(equipmentResponse)
        every { exerciseImagesApiService.getThumbnail(1) } returns Single.error(RuntimeException())

        dataSource.getExercises(0, 1).test()
            .assertError(RuntimeException::class.java)
    }
}