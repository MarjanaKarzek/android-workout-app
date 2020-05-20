package com.karzek.exercises.data.exercise

import androidx.paging.PagedList
import com.karzek.exercises.domain.exercise.model.BoundaryState
import com.karzek.exercises.domain.exercise.model.BoundaryState.BoundaryStateEndItem
import com.karzek.exercises.domain.exercise.model.BoundaryState.BoundaryStateZeroItems
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.domain.exercise.model.LoadingState
import io.reactivex.subjects.PublishSubject

class ExerciseBoundaryCallback : PagedList.BoundaryCallback<Exercise>() {

    val boundaryState = PublishSubject.create<BoundaryState>()

    override fun onItemAtFrontLoaded(itemAtFront: Exercise) {
        //ignore as we only append
    }

    override fun onZeroItemsLoaded() {
        boundaryState.onNext(BoundaryStateZeroItems)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Exercise) {
        boundaryState.onNext(BoundaryStateEndItem)
    }


    companion object {
        const val PAGE_SIZE = 5
    }
}