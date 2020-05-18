package com.karzek.exercises.ui.overview

import com.karzek.core.ui.BaseViewModel
import com.karzek.core.util.doOnIoObserveOnMain
import com.karzek.exercises.domain.IGetExercisesUseCase
import com.karzek.exercises.domain.IGetExercisesUseCase.Input
import com.karzek.exercises.domain.IGetExercisesUseCase.Output.Success
import com.karzek.exercises.domain.model.Exercise
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ExercisesViewModel @Inject constructor(
    private val getExercisesUseCase: IGetExercisesUseCase
) : BaseViewModel() {

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 0

    val exercises = BehaviorSubject.create<List<Exercise>>()

    fun getInitialExercises() {
        loadMoreItems()
    }

    fun onScroll(
        visibleItemCount: Int,
        filteredItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPosition: Int
    ) {
        if (!isLoading && !isLastPage) {
            if (areMoreItemsAvailable(visibleItemCount, filteredItemCount, totalItemCount, firstVisibleItemPosition)) {
                loadMoreItems()
            }
        }
    }

    private fun loadMoreItems() {
        isLoading = true
        getExercisesUseCase.execute(Input(currentPage, PAGE_SIZE))
            .doOnIoObserveOnMain()
            .subscribeBy { output ->
                when (output) {
                    is Success -> {
                        currentPage += 1
                        isLastPage = output.isLastPage
                        exercises.onNext(output.exercises)
                    }
                    else -> {
                        //TODO error handling
                    }
                }
                isLoading = false
            }
            .addTo(compositeDisposable)
    }

    private fun areMoreItemsAvailable(
        visibleItemCount: Int,
        filteredItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPosition: Int
    ): Boolean {
        return if (filteredItemCount == totalItemCount) {
            visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
        } else {
            visibleItemCount + firstVisibleItemPosition >= filteredItemCount
                && firstVisibleItemPosition >= 0
        }
    }

    companion object {
        private const val PAGE_SIZE = 5
    }
}