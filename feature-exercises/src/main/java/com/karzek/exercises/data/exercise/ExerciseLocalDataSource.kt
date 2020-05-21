package com.karzek.exercises.data.exercise

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.karzek.exercises.data.exercise.ExerciseBoundaryCallback.Companion.PAGE_SIZE
import com.karzek.exercises.data.exercise.contract.IExerciseLocalDataSource
import com.karzek.exercises.data.exercise.model.ExerciseRaw
import com.karzek.exercises.database.exercise.ExerciseDao
import com.karzek.exercises.database.exercise.ExerciseEntity
import com.karzek.exercises.database.relationship.ExerciseCategoryCrossRef
import com.karzek.exercises.database.relationship.ExerciseEquipmentCrossRef
import com.karzek.exercises.database.relationship.ExerciseMuscleCrossRef
import com.karzek.exercises.database.relationship.ExerciseRelationsDao
import com.karzek.exercises.domain.exercise.model.BoundaryState
import com.karzek.exercises.domain.exercise.model.Exercise
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ExerciseLocalDataSource @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val exerciseRelationsDao: ExerciseRelationsDao
) : IExerciseLocalDataSource {

    private val boundaryCallback = ExerciseBoundaryCallback()
    private val pageListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(PAGE_SIZE)
        .setPageSize(PAGE_SIZE)
        .build()

    override fun getBoundaryState(): PublishSubject<BoundaryState> {
        return boundaryCallback.boundaryState
    }

    override fun getItemCount(): Single<Int> {
        return exerciseDao.countItems()
    }

    override fun getExercises(): Observable<PagedList<Exercise>> {
        return Single.fromCallable {
            exerciseDao.allExercises().map { it.toModel() }
        }
            .flatMapObservable { dataSourceFactory ->
                RxPagedListBuilder(dataSourceFactory, pageListConfig)
                    .setBoundaryCallback(boundaryCallback)
                    .buildObservable()
            }
    }

    override fun addExercises(exercises: List<ExerciseRaw>): Completable {
        return insertExercises(exercises)
            .andThen(insertRelatedCategories(exercises))
            .andThen(insertRelatedMuscles(exercises))
            .andThen(insertRelatedEquipments(exercises))
    }

    override fun clearCache(): Completable {
        return exerciseDao.deleteAll()
            .andThen(exerciseRelationsDao.deleteAllEquipmentRelations())
            .andThen(exerciseRelationsDao.deleteAllMuscleRelations())
            .andThen(exerciseRelationsDao.deleteAllCategoryRelations())
    }

    private fun insertExercises(exercises: List<ExerciseRaw>): Completable {
        return exerciseDao.insertAll(exercises.map { ExerciseEntity(it.id, it.name, it.description, it.imageThumbnailUrl) })
    }

    private fun insertRelatedCategories(exercises: List<ExerciseRaw>): Completable {
        return exerciseRelationsDao.insertAllRelatedCategories(
            exercises.map { ExerciseCategoryCrossRef(it.id.toLong(), it.categoryId.toLong()) })
    }

    private fun insertRelatedMuscles(exercises: List<ExerciseRaw>): Completable {
        val relations = mutableListOf<ExerciseMuscleCrossRef>()
        for (exercise in exercises) {
            val something = exercise.muscleIds?.map { ExerciseMuscleCrossRef(exercise.id.toLong(), it.toLong()) }
            something?.run {
                relations.addAll(this)
            }
        }
        return if (relations.isNotEmpty()) {
            exerciseRelationsDao.insertAllRelatedMuscles(relations)
        } else {
            Completable.complete()
        }
    }

    private fun insertRelatedEquipments(exercises: List<ExerciseRaw>): Completable {
        val relations = mutableListOf<ExerciseEquipmentCrossRef>()
        for (exercise in exercises) {
            val something = exercise.muscleIds?.map { ExerciseEquipmentCrossRef(exercise.id.toLong(), it.toLong()) }
            something?.run {
                relations.addAll(this)
            }
        }
        return if (relations.isNotEmpty()) {
            exerciseRelationsDao.insertAllRelatedEquipments(relations)
        } else {
            Completable.complete()
        }
    }

}