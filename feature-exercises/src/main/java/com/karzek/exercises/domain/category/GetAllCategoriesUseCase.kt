package com.karzek.exercises.domain.category

import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase.Input
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase.Output
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase.Output.ErrorUnknown
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase.Output.Success
import com.karzek.exercises.domain.category.repository.ICategoryRepository
import io.reactivex.Single
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val categoryRepository: ICategoryRepository
) : IGetAllCategoriesUseCase {

    override fun execute(input: Input): Single<Output> {
        return categoryRepository.getAllCategories()
            .map {
                Success(it) as Output
            }
            .onErrorReturn {
                ErrorUnknown
            }
    }

}