package com.karzek.exercises.di

import com.karzek.exercises.data.category.CategoryLocalDataSource
import com.karzek.exercises.data.category.CategoryRemoteDataSource
import com.karzek.exercises.data.category.CategoryRepository
import com.karzek.exercises.data.category.contract.ICategoryLocalDataSource
import com.karzek.exercises.data.category.contract.ICategoryRemoteDataSource
import com.karzek.exercises.domain.category.GetAllCategoriesUseCase
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase
import com.karzek.exercises.domain.category.repository.ICategoryRepository
import dagger.Binds
import dagger.Module

@Module
interface CategoryModule {
    @Binds
    fun bindGetAllCategoriesUseCase(getAllCategoriesUseCase: GetAllCategoriesUseCase): IGetAllCategoriesUseCase

    @Binds
    fun bindCategoryRepository(categoryRepository: CategoryRepository): ICategoryRepository

    @Binds
    fun bindCategoryRemoteDataSource(categoryRemoteDataSource: CategoryRemoteDataSource): ICategoryRemoteDataSource

    @Binds
    fun bindCategoryLocalDataSource(categoryLocalDataSource: CategoryLocalDataSource): ICategoryLocalDataSource

}