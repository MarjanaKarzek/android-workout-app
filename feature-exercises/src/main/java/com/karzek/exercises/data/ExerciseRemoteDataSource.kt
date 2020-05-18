package com.karzek.exercises.data

import com.karzek.exercises.domain.model.Exercise
import com.karzek.exercises.http.category.ICategoryApiService
import com.karzek.exercises.http.category.model.CategoriesResponse
import com.karzek.exercises.http.category.model.CategoryResponse
import com.karzek.exercises.http.equipment.IEquipmentApiService
import com.karzek.exercises.http.equipment.model.EquipmentResponse
import com.karzek.exercises.http.equipment.model.EquipmentsResponse
import com.karzek.exercises.http.exercise.IExerciseApiService
import com.karzek.exercises.http.exercise.IExerciseImagesApiService
import com.karzek.exercises.http.exercise.model.ExerciseResponse
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse.ThumbnailResponse
import com.karzek.exercises.http.exercise.model.ExercisesResponse
import com.karzek.exercises.http.muscle.IMuscleApiService
import com.karzek.exercises.http.muscle.model.MuscleResponse
import com.karzek.exercises.http.muscle.model.MusclesResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function4
import javax.inject.Inject

class ExerciseRemoteDataSource @Inject constructor(
    private val exerciseApiService: IExerciseApiService,
    private val exerciseImagesApiService: IExerciseImagesApiService,
    private val muscleApiService: IMuscleApiService,
    private val equipmentApiService: IEquipmentApiService,
    private val categoryApiService: ICategoryApiService
) : IExerciseRemoteDataSource {

    override fun getExercises(
        currentPage: Int,
        pageSize: Int
    ): Single<Pair<List<Exercise>, Boolean>> {
        var isLastPage = false
        return Single.zip(
            exerciseApiService.getExercises(page = currentPage + 1, limit = pageSize),
            muscleApiService.getMuscles(),
            equipmentApiService.getEquipments(),
            categoryApiService.getCategories(),
            Function4<ExercisesResponse, MusclesResponse, EquipmentsResponse, CategoriesResponse, List<Exercise>> { exercisesResponse, musclesResponse, equipmentResponse, categoryResponse ->
                isLastPage = exercisesResponse.nextPage == null
                constructExerciseModel(
                    exercisesResponse.exercises,
                    musclesResponse.muscles,
                    equipmentResponse.equipments,
                    categoryResponse.categories
                )
            })
            .flatMap { addThumbnailImages(it) }
            .map {
                Pair(it, isLastPage)
            }

    }

    override fun getImageUrls(exerciseId: Int): Single<List<String>> {
        return exerciseImagesApiService.getExerciseImages(exerciseId)
            .map { response ->
                response.images.map { it.imageUrl }
            }
    }

    private fun addThumbnailImages(exercises: List<Exercise>): Single<List<Exercise>> {
        return Observable.fromIterable(exercises)
            .flatMapSingle { exercise ->
                exerciseImagesApiService.getThumbnail(exercise.id)
                    .map {
                        exercise.apply {
                            if (it is ThumbnailResponse) {
                                imageThumbnailUrl = it.original
                            }
                        }
                    }
            }
            .toList()
    }

    private fun constructExerciseModel(
        exercises: List<ExerciseResponse>,
        muscles: List<MuscleResponse>,
        equipments: List<EquipmentResponse>,
        categories: List<CategoryResponse>
    ): List<Exercise> {
        return exercises.map {
            Exercise(
                it.id,
                it.name,
                getCategory(categories, it.categoryId),
                it.description,
                muscles = getMuscles(muscles, it.muscleIds),
                equipment = getEquipments(equipments, it.equipmentIds)
            )
        }
    }

    private fun getCategory(
        categories: List<CategoryResponse>,
        categoryId: Int
    ): String {
        return categories.first { it.id == categoryId }.name
    }

    private fun getMuscles(
        muscles: List<MuscleResponse>,
        muscleIds: List<Int>
    ): List<String> {
        return muscleIds.map { currentId ->
            muscles.first { it.id == currentId }.name
        }
    }

    private fun getEquipments(
        equipments: List<EquipmentResponse>,
        equipmentIds: List<Int>
    ): List<String> {
        return equipmentIds.map { currentId ->
            equipments.first { it.id == currentId }.name
        }
    }

}