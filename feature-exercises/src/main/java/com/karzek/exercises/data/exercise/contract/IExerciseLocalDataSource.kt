package com.karzek.exercises.data.exercise.contract

import androidx.paging.PagedList
import com.karzek.exercises.data.exercise.model.ExerciseRaw
import com.karzek.exercises.domain.exercise.model.BoundaryState
import com.karzek.exercises.domain.exercise.model.Exercise
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface IExerciseLocalDataSource {
    fun getExercises(): Observable<PagedList<Exercise>>
    fun addExercises(exercises: List<ExerciseRaw>): Completable
    fun getBoundaryState(): PublishSubject<BoundaryState>
    fun getItemCount(): Single<Int>
    fun clearCache(): Completable
}
