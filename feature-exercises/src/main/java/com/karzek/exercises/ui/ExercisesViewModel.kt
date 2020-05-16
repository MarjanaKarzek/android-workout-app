package com.karzek.exercises.ui

import com.karzek.core.ui.BaseViewModel
import com.karzek.exercises.domain.model.Exercise
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ExercisesViewModel @Inject constructor() : BaseViewModel() {

    private val exercisesMock = listOf(
        Exercise("Exercise 1"),
        Exercise("Exercise 2"),
        Exercise("Exercise 3"),
        Exercise("Exercise 4"),
        Exercise("Exercise 5"),
        Exercise("Exercise 6")
    )

    val exercises = BehaviorSubject.create<List<Exercise>>()

    fun getExercises() {
        exercises.onNext(exercisesMock)
    }
}