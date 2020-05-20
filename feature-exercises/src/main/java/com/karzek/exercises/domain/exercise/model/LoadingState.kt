package com.karzek.exercises.domain.exercise.model

sealed class LoadingState {
    object Success : LoadingState()
    object Loading : LoadingState()
    object Error : LoadingState()
}