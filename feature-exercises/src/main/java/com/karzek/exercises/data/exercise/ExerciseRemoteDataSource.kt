package com.karzek.exercises.data.exercise

import com.karzek.exercises.data.exercise.contract.IExerciseRemoteDataSource
import com.karzek.exercises.data.exercise.model.ExerciseRaw
import com.karzek.exercises.http.exercise.IExerciseApiService
import com.karzek.exercises.http.exercise.IExerciseImagesApiService
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse.ThumbnailResponse
import com.karzek.exercises.http.exercise.model.toModels
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class ExerciseRemoteDataSource @Inject constructor(
    private val exerciseApiService: IExerciseApiService,
    private val exerciseImagesApiService: IExerciseImagesApiService
) : IExerciseRemoteDataSource {
    private var queryFilter: String? = null
    private var categoryFilter: Int? = null

    override fun getExercises(
        currentPage: Int,
        pageSize: Int
    ): Single<Pair<List<ExerciseRaw>, Boolean>> {
        var isLastPage = false
        return exerciseApiService.getExercises(page = currentPage + 1, limit = pageSize, query = queryFilter, category = categoryFilter)
            .map { response ->
                isLastPage = response.nextPage == null
                response.exercises.toModels()
            }
            .flatMap {
                addThumbnailImages(it)
            }
            .map {
                Pair(it, isLastPage)
            }
    }

    override fun setQueryFilter(queryFilter: String?): Completable {
        if (queryFilter.isNullOrBlank()) {
            this.queryFilter = null
        } else {
            this.queryFilter = queryFilter
        }
        return Completable.complete()
    }

    override fun setCategoryFilter(id: Int?): Completable {
        categoryFilter = id
        return Completable.complete()
    }

    private fun addThumbnailImages(exercises: List<ExerciseRaw>): Single<List<ExerciseRaw>> {
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

}