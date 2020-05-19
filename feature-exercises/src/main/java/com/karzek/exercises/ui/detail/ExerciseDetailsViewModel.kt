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

    val exerciseDetails = BehaviorSubject.create<Exercise>()
    val exerciseImages = BehaviorSubject.create<List<String>>()
    val loading = BehaviorSubject.create<Boolean>()

    fun setExercise(exercise: Exercise) {
        loading.onNext(true)
        exerciseDetails.onNext(exercise)
        getImagesForExerciseUseCase.execute(Input(exercise.id))
            .doOnIoObserveOnMain()
            .subscribeBy { output ->
                when (output) {
                    is Success -> exerciseImages.onNext(output.imageUrls)
                    is SuccessNoData -> exerciseImages.onComplete()
                    else -> {
                        //TODO error handling
                    }
                }
                loading.onNext(false)
            }
            .addTo(compositeDisposable)
    }
}