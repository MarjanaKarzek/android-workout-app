package com.karzek.exercises.domain

import com.karzek.core.domain.BaseSingleUseCase
import com.karzek.core.domain.BaseUseCase
import com.karzek.exercises.domain.IGetExercisesUseCase.Input
import com.karzek.exercises.domain.IGetExercisesUseCase.Output
import com.karzek.exercises.domain.model.Exercise

interface IGetExercisesUseCase : BaseSingleUseCase<Input, Output> {

    data class Input(
        val currentPage: Int,
        val pageSize: Int
    ) : BaseUseCase.Input

    sealed class Output : BaseUseCase.Output {
        data class Success(
            val exercises: List<Exercise>,
            val isLastPage: Boolean
        ) : Output()
        object ErrorUnknown : Output()
    }

}