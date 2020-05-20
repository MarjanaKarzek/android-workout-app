package com.karzek.exercises.data.category.contract

import com.karzek.exercises.domain.category.model.Category
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface ICategoryLocalDataSource {
    fun getAllCategories(): Maybe<List<Category>>
    fun setAllCategories(categories: List<Category>): Completable
    fun isCacheValid(): Single<Boolean>
}
