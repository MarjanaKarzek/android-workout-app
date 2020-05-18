package com.karzek.exercises.ui.detail

import com.karzek.core.ui.BaseViewModel
import com.karzek.core.util.doOnIoObserveOnMain
import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase
import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase.Input
import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase.Output.Success
import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase.Output.SuccessNoData
import com.karzek.exercises.domain.exercise.model.Exercise
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ExerciseDetailsViewModel @Inject constructor(
    private val getImagesForExerciseUseCase: IGetImagesForExerciseUseCase
) : BaseViewModel() {

    val exerciseDetails = BehaviorSubject.create<Pair<Exercise, List<String>>>()

    fun setExercise(exercise: Exercise) {
        getImagesForExerciseUseCase.execute(Input(exercise.id))
            .doOnIoObserveOnMain()
            .subscribeBy { output ->
                when (output) {
                    is Success -> exerciseDetails.onNext(exercise to output.imageUrls)
                    is SuccessNoData -> exerciseDetails.onNext(exercise to emptyList())
                    else -> {
                        //TODO error handling
                    }
                }
            }
            .addTo(compositeDisposable)
    }
}