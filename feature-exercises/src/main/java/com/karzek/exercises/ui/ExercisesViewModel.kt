package com.karzek.exercises.ui

import com.karzek.core.ui.BaseViewModel
import com.karzek.exercises.domain.model.Exercise
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ExercisesViewModel @Inject constructor() : BaseViewModel() {

    private val exercisesMock = listOf(
        Exercise(
            "Exercise 1",
            "Category 1"
        ),
        Exercise(
            "Exercise 2",
            "Category 2",
            imageUrl = "https://wger.de/media/exercise-images/91/Crunches-2.png.800x800_q90.png"
        ),
        Exercise(
            "Exercise 3",
            "Category 3",
            muscles = listOf("Anterior deltoid")
        ),
        Exercise(
            "Exercise 4",
            "Category 4",
            equipment = listOf("Barbell", "Incline bench")
        ),
        Exercise(
            "Exercise 5",
            "Category 5",
            muscles = listOf("Anterior deltoid"),
            equipment = listOf("Barbell", "Incline bench")
        ),
        Exercise(
            "Exercise 6",
            "Category 6",
            "https://wger.de/media/exercise-images/91/Crunches-2.png.800x800_q90.png",
            listOf("Anterior deltoid"),
            listOf("Barbell", "Incline bench")
        )
    )

    val exercises = BehaviorSubject.create<List<Exercise>>()

    fun getExercises() {
        exercises.onNext(exercisesMock)
    }
}