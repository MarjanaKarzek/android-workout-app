package com.karzek.exercises.http.category

import com.karzek.exercises.http.category.model.CategoriesResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ICategoryApiService {
    @GET("exercisecategory/")
    fun getCategories(): Single<CategoriesResponse>
}