package com.karzek.exercises.domain.exercise.repository

import androidx.paging.PagedList
import com.karzek.exercises.domain.exercise.model.BoundaryState
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.domain.exercise.model.LoadingState
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface IPagedExerciseProvider {

    val exerciseLoadingState: BehaviorSubject<LoadingState>

    fun getExercises(): Observable<PagedList<Exercise>>

    fun appendExercises(): BehaviorSubject<LoadingState>

    fun clearCache() : Completable

    fun getBoundaryState(): PublishSubject<BoundaryState>

    fun setQueryFilter(queryFilter: String?): Completable

    fun setCategoryFilter(id: Int?): Completable

}
