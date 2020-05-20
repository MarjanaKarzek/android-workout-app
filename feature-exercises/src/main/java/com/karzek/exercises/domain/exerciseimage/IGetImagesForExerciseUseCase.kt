package com.karzek.exercises.domain.exerciseimage

import com.karzek.core.domain.BaseSingleUseCase
import com.karzek.core.domain.BaseUseCase
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Input
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Output

interface IGetImagesForExerciseUseCase : BaseSingleUseCase<Input, Output> {

    data class Input(val exerciseId: Int) : BaseUseCase.Input

    sealed class Output : BaseUseCase.Output {
        data class Success(val imageUrls: List<String>) : Output()
        object SuccessNoData : Output()
        object ErrorUnknown : Output()
    }

}