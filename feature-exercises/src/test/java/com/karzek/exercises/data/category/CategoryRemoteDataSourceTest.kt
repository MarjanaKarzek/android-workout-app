package com.karzek.exercises.data.category

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.category.contract.ICategoryRemoteDataSource
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.http.category.ICategoryApiService
import com.karzek.exercises.http.category.model.CategoriesResponse
import com.karzek.exercises.http.category.model.CategoryResponse
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CategoryRemoteDataSourceTest : BaseUnitTest() {

    private val categoryApiService: ICategoryApiService = mockk()

    private lateinit var dataSource: ICategoryRemoteDataSource

    private val categoryResponses = listOf(
        CategoryResponse(0, "Category 1"),
        CategoryResponse(1, "Category 2")
    )

    private val categories = listOf(
        Category(0, "Category 1"),
        Category(1, "Category 2")
    )

    @BeforeEach
    override fun setup() {
        super.setup()
        dataSource = CategoryRemoteDataSource(categoryApiService)
    }

    @Test
    fun `getAllCategories returns expected output`() {
        every { categoryApiService.getCategories() } returns Single.just(CategoriesResponse(categoryResponses))

        dataSource.getAllCategories().test()
            .assertValue(categories)
    }

    @Test
    fun `exceptions from api service get passed to observer`() {
        every { categoryApiService.getCategories() } returns Single.error(RuntimeException())

        dataSource.getAllCategories().test()
            .assertError(RuntimeException::class.java)
    }

}