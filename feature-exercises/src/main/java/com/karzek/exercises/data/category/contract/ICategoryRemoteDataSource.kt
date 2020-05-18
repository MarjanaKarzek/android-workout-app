package com.karzek.exercises.data.category.contract

import com.karzek.exercises.domain.category.model.Category
import io.reactivex.Single

interface ICategoryRemoteDataSource {
    fun getAllCategories(): Single<List<Category>>
}
