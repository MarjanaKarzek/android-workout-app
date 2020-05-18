package com.karzek.exercises.domain.exercise

import com.karzek.core.domain.BaseSingleUseCase
import com.karzek.core.domain.BaseUseCase
import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase.Input
import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase.Output

interface IGetImagesForExerciseUseCase : BaseSingleUseCase<Input, Output> {

    data class Input(val exerciseId: Int) : BaseUseCase.Input

    sealed class Output : BaseUseCase.Output {
        data class Success(val imageUrls: List<String>) : Output()
        object SuccessNoData : Output()
        object ErrorUnknown : Output()
    }

}