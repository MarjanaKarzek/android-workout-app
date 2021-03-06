package com.karzek.exercises.ui.detail

import com.karzek.core.ui.BaseViewModel
import com.karzek.core.ui.error.UIError.NetworkConnection
import com.karzek.core.util.doOnIoObserveOnMain
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Input
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Output.Success
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Output.SuccessNoData
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ExerciseDetailsViewModel @Inject constructor(
    private val getImagesForExerciseUseCase: IGetImagesForExerciseUseCase
) : BaseViewModel() {

    private lateinit var exercise: Exercise

    val exerciseDetails = BehaviorSubject.create<Exercise>()
    val exerciseImages = BehaviorSubject.create<List<String>>()
    val loading = BehaviorSubject.create<Boolean>()

    fun setExercise(exercise: Exercise) {
        loading.onNext(true)
        this.exercise = exercise
        exerciseDetails.onNext(exercise)
        loadImages()
    }

    fun loadImages() {
        loading.onNext(true)
        getImagesForExerciseUseCase.execute(Input(exercise.id))
            .doOnIoObserveOnMain()
            .subscribeBy { output ->
                when (output) {
                    is Success -> exerciseImages.onNext(output.imageUrls)
                    is SuccessNoData -> exerciseImages.onComplete()
                    else -> error.onNext(NetworkConnection)
                }
                loading.onNext(false)
            }
            .addTo(compositeDisposable)
    }
}