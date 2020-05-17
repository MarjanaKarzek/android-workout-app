package com.karzek.exercises.ui.overview.adapter

import com.karzek.exercises.domain.model.Exercise

interface ExerciseInteractionListener {
    fun onExerciseClicked(exercise: Exercise)
}