package com.karzek.exercises.domain.exercise.model

sealed class BoundaryState {
    object BoundaryStateZeroItems : BoundaryState()
    object BoundaryStateEndItem : BoundaryState()
}