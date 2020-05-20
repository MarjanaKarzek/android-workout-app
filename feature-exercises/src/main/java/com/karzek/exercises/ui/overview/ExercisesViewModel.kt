package com.karzek.exercises.ui.overview

import com.karzek.core.ui.BaseViewModel
import com.karzek.core.util.doOnIoObserveOnMain
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.domain.exercise.IGetExercisesUseCase
import com.karzek.exercises.domain.exercise.IGetExercisesUseCase.Input
import com.karzek.exercises.domain.exercise.IGetExercisesUseCase.Output.Success
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.ui.overview.error.NetworkErrorOnLoadingItems
import com.karzek.exercises.ui.overview.error.NetworkErrorOnViewInit
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase.Output.Success as SuccessCategories

class ExercisesViewModel @Inject constructor(
    private val getExercisesUseCase: IGetExercisesUseCase,
    private val getAllCategoriesUseCase: IGetAllCategoriesUseCase
) : BaseViewModel() {

    private var isLoadingExercises = false
    private var isLastPage = false
    private var currentPage = 0

    val exercises = BehaviorSubject.create<List<Exercise>>()
    val filterOptions = BehaviorSubject.create<List<Category>>()
    val viewLoading = BehaviorSubject.create<Boolean>()
    val listLoading = BehaviorSubject.create<Boolean>()

    fun retrieveInitialData() {
        viewLoading.onNext(true)
        isLoadingExercises = true
        getAllCategoriesUseCase.execute(IGetAllCategoriesUseCase.Input)
            .doOnIoObserveOnMain()
            .subscribeBy { output ->
                when (output) {
                    is SuccessCategories -> {
                        loadMoreItems()
                        filterOptions.onNext(output.categories)
                    }
                    else -> {
                        viewLoading.onNext(false)
                        error.onNext(NetworkErrorOnViewInit)
                    }
                }
            }
            .addTo(compositeDisposable)
    }

    private fun loadMoreItems() {
        getExercisesUseCase.execute(Input(currentPage, PAGE_SIZE))
            .doOnIoObserveOnMain()
            .subscribeBy { output ->
                isLoadingExercises = false
                when (output) {
                    is Success -> {
                        currentPage += 1
                        isLastPage = output.isLastPage
                        exercises.onNext(output.exercises)
                    }
                    else -> error.onNext(NetworkErrorOnLoadingItems)
                }
                listLoading.onNext(false)
                viewLoading.onNext(false)
            }
            .addTo(compositeDisposable)
    }

    fun checkForMoreItems(
        visibleItemCount: Int,
        filteredItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPosition: Int,
        isScrollDirectionDown: Boolean
    ) {
        if (!isLoadingExercises && !isLastPage) {
            if (
                areMoreItemsAvailable(
                    visibleItemCount,
                    filteredItemCount,
                    totalItemCount,
                    firstVisibleItemPosition,
                    isScrollDirectionDown
                )
            ) {
                listLoading.onNext(true)
                isLoadingExercises = true
                loadMoreItems()
            }
        }
    }

    private fun areMoreItemsAvailable(
        visibleItemCount: Int,
        filteredItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPosition: Int,
        isScrollDirectionDown: Boolean
    ): Boolean {
        return if (filteredItemCount == totalItemCount) {
            //check for full list of exercises
            isScrollDirectionDown && visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
        } else {
            //check for filtered list of exercises
            filteredItemCount <= PAGE_SIZE || isScrollDirectionDown && visibleItemCount + firstVisibleItemPosition >= filteredItemCount
                && firstVisibleItemPosition >= 0
        }
    }

    companion object {
        private const val PAGE_SIZE = 5
    }
}