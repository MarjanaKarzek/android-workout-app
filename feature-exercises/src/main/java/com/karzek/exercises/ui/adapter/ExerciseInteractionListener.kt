package com.karzek.exercises.ui.adapter

import com.karzek.exercises.domain.model.Exercise

interface ExerciseInteractionListener {
    fun onExerciseClicked(exercise: Exercise)
}