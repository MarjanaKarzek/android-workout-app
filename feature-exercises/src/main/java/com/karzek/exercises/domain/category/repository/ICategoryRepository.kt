package com.karzek.exercises.domain.category.repository

import com.karzek.exercises.domain.category.model.Category
import io.reactivex.Completable
import io.reactivex.Single

interface ICategoryRepository {
    fun validateCache(): Completable
    fun getAllCategories(): Single<List<Category>>
}
