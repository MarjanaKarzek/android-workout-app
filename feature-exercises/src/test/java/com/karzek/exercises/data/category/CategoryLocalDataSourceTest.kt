package com.karzek.exercises.data.category

import android.content.SharedPreferences
import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.category.contract.ICategoryLocalDataSource
import com.karzek.exercises.database.category.CategoryDao
import com.karzek.exercises.database.category.CategoryEntity
import com.karzek.exercises.domain.category.model.Category
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

internal class CategoryLocalDataSourceTest : BaseUnitTest() {

    private val sharedPreferences: SharedPreferences = mockk()
    private val categoryDao: CategoryDao = mockk(relaxed = true)

    private lateinit var dataSource: ICategoryLocalDataSource

    private val categoriesEntities = listOf(
        CategoryEntity(0, "Category 1"),
        CategoryEntity(1, "Category 2")
    )

    private val categories = listOf(
        Category(0, "Category 1"),
        Category(1, "Category 2")
    )

    @BeforeEach
    override fun setup() {
        super.setup()
        dataSource = CategoryLocalDataSource(sharedPreferences, categoryDao)
    }

    @Test
    fun `getAllCategories returns expected output when cache is valid`() {
        val currentTime = Date().time
        every { sharedPreferences.getLong("CATEGORY_CACHE_TIME_STAMP", any()) } returns currentTime
        every { categoryDao.getAll() } returns Single.just(categoriesEntities)

        dataSource.getAllCategories().test()
            .assertValue(categories)
    }

    @Test
    fun `getAllCategories returns Maybe empty output when cache is invalid`() {
        val currentTime = Date().time + 24 * 60 * 60 * 1000
        every { sharedPreferences.getLong("CATEGORY_CACHE_TIME_STAMP", any()) } returns currentTime

        dataSource.getAllCategories().test()
            .assertEmpty()
    }

    @Test
    fun `exceptions from dao get passed to observer`() {
        val currentTime = Date().time
        every { sharedPreferences.getLong("CATEGORY_CACHE_TIME_STAMP", any()) } returns currentTime
        every { categoryDao.getAll() } returns Single.error(RuntimeException())

        dataSource.getAllCategories().test()
            .assertError(RuntimeException::class.java)
    }
}