package com.karzek.exercises.data.category

import com.karzek.exercises.data.category.contract.ICategoryLocalDataSource
import com.karzek.exercises.data.category.contract.ICategoryRemoteDataSource
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.domain.category.repository.ICategoryRepository
import io.reactivex.Single
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryRemoteDataSource: ICategoryRemoteDataSource,
    private val categoryLocalDataSource: ICategoryLocalDataSource
) : ICategoryRepository {

    override fun getAllCategories(): Single<List<Category>> {
        return categoryLocalDataSource.getAllCategories()
            .switchIfEmpty(
                categoryRemoteDataSource.getAllCategories()
                    .flatMap { categories ->
                        if(categories.isEmpty()) {
                            throw IllegalStateException("categories cannot be empty")
                        }
                        categoryLocalDataSource.setAllCategories(categories)
                            .andThen(Single.just(categories))
                    }
            )
    }

}