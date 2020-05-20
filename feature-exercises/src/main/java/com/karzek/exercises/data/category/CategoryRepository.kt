package com.karzek.exercises.data.category

import com.karzek.exercises.data.category.contract.ICategoryLocalDataSource
import com.karzek.exercises.data.category.contract.ICategoryRemoteDataSource
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.domain.category.repository.ICategoryRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryRemoteDataSource: ICategoryRemoteDataSource,
    private val categoryLocalDataSource: ICategoryLocalDataSource
) : ICategoryRepository {

    override fun validateCache(): Completable {
        return categoryLocalDataSource.isCacheValid()
            .flatMapCompletable { isValid ->
                if (isValid) {
                    Completable.complete()
                } else {
                    updateCache()
                }
            }
    }

    private fun updateCache(): Completable {
        return categoryRemoteDataSource.getAllCategories()
            .flatMapCompletable {
                categoryLocalDataSource.setAllCategories(it)
            }
    }

    override fun getAllCategories(): Single<List<Category>> {
        return categoryLocalDataSource.getAllCategories()
            .switchIfEmpty(
                categoryRemoteDataSource.getAllCategories()
                    .flatMap { categories ->
                        categoryLocalDataSource.setAllCategories(categories)
                            .andThen(Single.just(categories))
                    }
            )
    }

}