package com.karzek.exercises.domain.validation

import com.karzek.core.domain.BaseSingleUseCase
import com.karzek.core.domain.BaseUseCase
import com.karzek.exercises.domain.validation.IValidateCacheUseCase.Input
import com.karzek.exercises.domain.validation.IValidateCacheUseCase.Output

interface IValidateCacheUseCase : BaseSingleUseCase<Input, Output> {

    object Input : BaseUseCase.Input

    sealed class Output : BaseUseCase.Output {
        object Success : Output()
        object ErrorUnknown : Output()
    }

}