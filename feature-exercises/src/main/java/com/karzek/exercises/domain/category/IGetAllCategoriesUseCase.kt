package com.karzek.exercises.domain.category

import com.karzek.core.domain.BaseSingleUseCase
import com.karzek.core.domain.BaseUseCase
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase.Input
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase.Output
import com.karzek.exercises.domain.category.model.Category

interface IGetAllCategoriesUseCase : BaseSingleUseCase<Input, Output> {

    object Input : BaseUseCase.Input

    sealed class Output : BaseUseCase.Output {
        data class Success(val categories: List<Category>) : Output()
        object ErrorUnknown : Output()
    }

}