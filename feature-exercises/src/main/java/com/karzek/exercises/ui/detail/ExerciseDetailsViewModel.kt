package com.karzek.exercises.ui.detail

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

class ExerciseDetailsViewModel @Inject constructor() : BaseViewModel() {

    val exerciseDetails = BehaviorSubject.create<Pair<Exercise, List<String>>>()

    fun setExercise(exercise: Exercise) {
        //todo get images for exercise
        exerciseDetails.onNext(exercise to emptyList())
    }
}