package com.karzek.exercises.ui.overview

import androidx.paging.PagedList
import com.karzek.core.ui.BaseViewModel
import com.karzek.core.util.doOnIoObserveOnMain
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.domain.exercise.model.LoadingState
import com.karzek.exercises.domain.exercise.repository.IPagedExerciseProvider
import com.karzek.exercises.domain.validation.IValidateCacheUseCase
import com.karzek.exercises.domain.validation.IValidateCacheUseCase.Output.Success
import com.karzek.exercises.ui.overview.error.NetworkErrorOnViewInit
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase.Output.Success as SuccessCategories

class ExercisesViewModel @Inject constructor(
    private val validateCacheUseCase: IValidateCacheUseCase,
    private val getAllCategoriesUseCase: IGetAllCategoriesUseCase,
    private val pagedExerciseProvider: IPagedExerciseProvider
) : BaseViewModel() {

    init {
        initialize()
    }

    private var exercisesDataSource: Observable<PagedList<Exercise>>? = null

    val exercises: Observable<PagedList<Exercise>>
        get() {
            if (exercisesDataSource == null) {
                exercisesDataSource = pagedExerciseProvider.getExercises()
            }
            return exercisesDataSource ?: throw IllegalStateException("exercise data source was null")
        }

    val initialized = BehaviorSubject.create<Boolean>()
    val filterOptions = BehaviorSubject.create<List<Category>>()
    val loadingStatus: Observable<LoadingState> = pagedExerciseProvider.getBoundaryState()
        .switchMap {
            onBoundaryItemLoaded()
        }

    private fun onBoundaryItemLoaded(): BehaviorSubject<LoadingState> {
        return pagedExerciseProvider.appendExercises()
    }

    fun initialize() {
        validateCacheUseCase.execute(IValidateCacheUseCase.Input)
            .doOnIoObserveOnMain()
            .subscribeBy { output ->
                when (output) {
                    is Success -> {
                        setupFilterLabels()
                        initialized.onNext(true)
                    }
                    else -> {
                        error.onNext(NetworkErrorOnViewInit)
                    }
                }
            }
            .addTo(compositeDisposable)
    }

    private fun setupFilterLabels() {
        getAllCategoriesUseCase.execute(IGetAllCategoriesUseCase.Input)
            .doOnIoObserveOnMain()
            .subscribeBy { output ->
                when (output) {
                    is SuccessCategories -> {
                        filterOptions.onNext(output.categories)
                    }
                }
            }
            .addTo(compositeDisposable)
    }

    fun setQueryFilter(queryFilter: CharSequence?) {
        pagedExerciseProvider.setQueryFilter(queryFilter.toString())
            .doOnIoObserveOnMain()
            .subscribeBy {
                refreshUI()
            }
            .addTo(compositeDisposable)
    }

    fun setCategoryFilter(id: Int?) {
        pagedExerciseProvider.setCategoryFilter(id)
            .doOnIoObserveOnMain()
            .subscribeBy {
                refreshUI()
            }
            .addTo(compositeDisposable)
    }

    fun refreshUI() {
        exercisesDataSource = null
        initialized.onNext(true)
    }

}