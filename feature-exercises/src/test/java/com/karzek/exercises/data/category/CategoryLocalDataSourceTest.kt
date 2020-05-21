package com.karzek.exercises.data.category

import android.content.SharedPreferences
import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.category.contract.ICategoryLocalDataSource
import com.karzek.exercises.database.category.CategoryDao
import com.karzek.exercises.database.category.CategoryEntity
import com.karzek.exercises.domain.category.model.Category
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

internal class CategoryLocalDataSourceTest : BaseUnitTest() {

    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)
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
    fun `isCacheValid returns true when cache is valid`() {
        every { categoryDao.countItems() } returns Single.just(1)

        val currentTime = Date().time
        every { sharedPreferences.getLong("CATEGORY_CACHE_TIME_STAMP", any()) } returns currentTime

        dataSource.isCacheValid().test()
            .assertValue(true)
    }

    @Test
    fun `isCacheValid returns false when cache is invalid due to expired time stamp`() {
        every { categoryDao.countItems() } returns Single.just(1)

        every { sharedPreferences.getLong("CATEGORY_CACHE_TIME_STAMP", any()) } returns 0

        dataSource.isCacheValid().test()
            .assertValue(false)
    }

    @Test
    fun `isCacheValid returns false when cache is invalid due to empty table`() {
        every { categoryDao.countItems() } returns Single.just(0)

        dataSource.isCacheValid().test()
            .assertValue(false)
    }

    @Test
    fun `getAllCategories returns expected output when cache is valid`() {
        every { categoryDao.countItems() } returns Single.just(5)

        val currentTime = Date().time
        every { sharedPreferences.getLong("CATEGORY_CACHE_TIME_STAMP", any()) } returns currentTime
        every { categoryDao.getAll() } returns Single.just(categoriesEntities)

        dataSource.getAllCategories().test()
            .assertValue(categories)
    }

    @Test
    fun `getAllCategories returns Maybe empty output when cache is invalid due to expired time stamp`() {
        every { categoryDao.countItems() } returns Single.just(5)
        every { sharedPreferences.getLong("CATEGORY_CACHE_TIME_STAMP", any()) } returns 0

        dataSource.getAllCategories().test()
            .assertComplete()
    }

    @Test
    fun `getAllCategories returns Maybe empty output when cache is invalid due to empty table`() {
        every { categoryDao.countItems() } returns Single.just(0)

        dataSource.getAllCategories().test()
            .assertComplete()
    }

    @Test
    fun `setAllCategories removes the old data sets it to the new data and updates the timestamp`() {
        dataSource.setAllCategories(categories).test()
            .assertComplete()

        verify(exactly = 1) { categoryDao.deleteAll() }
        verify(exactly = 1) { categoryDao.insertAll(categoriesEntities) }
        verify(exactly = 1) { sharedPreferences.edit().putLong("CATEGORY_CACHE_TIME_STAMP", any()) }
    }

    @Test
    fun `exceptions from dao get passed to observer`() {
        val currentTime = Date().time
        every { sharedPreferences.getLong("CATEGORY_CACHE_TIME_STAMP", any()) } returns currentTime
        every { categoryDao.countItems() } returns Single.error(RuntimeException())

        dataSource.getAllCategories().test()
            .assertError(RuntimeException::class.java)
    }
}