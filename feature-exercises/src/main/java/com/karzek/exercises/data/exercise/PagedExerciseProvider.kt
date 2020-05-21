package com.karzek.exercises.data.exercise

import androidx.paging.PagedList
import com.karzek.core.util.doOnIoObserveOnMain
import com.karzek.exercises.data.exercise.ExerciseBoundaryCallback.Companion.PAGE_SIZE
import com.karzek.exercises.data.exercise.contract.IExerciseLocalDataSource
import com.karzek.exercises.data.exercise.contract.IExerciseRemoteDataSource
import com.karzek.exercises.domain.exercise.model.BoundaryState
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.domain.exercise.model.LoadingState
import com.karzek.exercises.domain.exercise.model.LoadingState.Error
import com.karzek.exercises.domain.exercise.model.LoadingState.Loading
import com.karzek.exercises.domain.exercise.model.LoadingState.Success
import com.karzek.exercises.domain.exercise.repository.IPagedExerciseProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class PagedExerciseProvider @Inject constructor(
    private val exerciseRemoteDataSource: IExerciseRemoteDataSource,
    private val exerciseLocalDataSource: IExerciseLocalDataSource
) : IPagedExerciseProvider {

    private var isLastPageReached = false
    private var currentPage = 0

    private val disposable = CompositeDisposable()

    override val exerciseLoadingState = BehaviorSubject.create<LoadingState>()

    override fun appendExercises(): BehaviorSubject<LoadingState> {
        if (!isLastPageReached && (exerciseLoadingState.value == null || exerciseLoadingState.value != Loading)) {
            exerciseLoadingState.onNext(Loading)
            fetchExercises()
                .doOnIoObserveOnMain()
                .subscribeBy(
                    onComplete = {
                        exerciseLoadingState.onNext(Success)
                    },
                    onError = {
                        Timber.e(it)
                        exerciseLoadingState.onNext(Error)
                    }
                )
                .addTo(disposable)
        }
        return exerciseLoadingState
    }

    private fun fetchExercises(): Completable {
        return exerciseRemoteDataSource.getExercises(currentPage, PAGE_SIZE)
            .flatMapCompletable {
                isLastPageReached = it.second
                currentPage += 1
                exerciseLocalDataSource.addExercises(it.first)
            }
    }

    override fun getExercises(): Observable<PagedList<Exercise>> {
        return exerciseLocalDataSource.getExercises()
    }

    override fun getBoundaryState(): PublishSubject<BoundaryState> {
        return exerciseLocalDataSource.getBoundaryState()
    }

    override fun setQueryFilter(queryFilter: String?): Completable {
        return exerciseRemoteDataSource.setQueryFilter(queryFilter)
            .andThen(clearCache())
    }

    override fun setCategoryFilter(id: Int?): Completable {
        return exerciseRemoteDataSource.setCategoryFilter(id)
            .andThen(clearCache())
    }

    override fun clearCache(): Completable {
        isLastPageReached = false
        currentPage = 0
        return exerciseLocalDataSource.clearCache()
    }
}