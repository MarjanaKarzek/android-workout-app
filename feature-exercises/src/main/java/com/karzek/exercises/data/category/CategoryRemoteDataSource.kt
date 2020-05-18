package com.karzek.exercises.data.category

import com.karzek.exercises.data.category.contract.ICategoryRemoteDataSource
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.http.category.ICategoryApiService
import com.karzek.exercises.http.category.model.toModels
import io.reactivex.Single
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val categoryApiService: ICategoryApiService
) : ICategoryRemoteDataSource {

    override fun getAllCategories(): Single<List<Category>> {
        return categoryApiService.getCategories()
            .map { it.categories.toModels() }
    }

}
