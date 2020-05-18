package com.karzek.exercises.domain.category.repository

import com.karzek.exercises.domain.category.model.Category
import io.reactivex.Single

interface ICategoryRepository {
    fun getAllCategories(): Single<List<Category>>
}
