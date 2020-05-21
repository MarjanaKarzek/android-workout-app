package com.karzek.exercises.data.category

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.category.contract.ICategoryLocalDataSource
import com.karzek.exercises.data.category.contract.ICategoryRemoteDataSource
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.domain.category.repository.ICategoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CategoryRepositoryTest : BaseUnitTest() {

    private val remoteDataSource: ICategoryRemoteDataSource = mockk(relaxed = true)
    private val localDataSource: ICategoryLocalDataSource = mockk()

    private val categories = listOf(
        Category(0, "Category 1"),
        Category(1, "Category 2")
    )

    private lateinit var repository: ICategoryRepository

    @BeforeEach
    override fun setup() {
        super.setup()
        repository = CategoryRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun `getAllCategories returns expected output from cache if cache is valid`() {
        every { localDataSource.getAllCategories() } returns Maybe.just(categories)

        repository.getAllCategories().test()
            .assertValue(categories)

        verify(exactly = 0) { localDataSource.setAllCategories(any()) }
    }

    @Test
    fun `getAllCategories returns expected output from remote if cache is invalid`() {
        every { localDataSource.getAllCategories() } returns Maybe.empty()
        every { localDataSource.setAllCategories(categories) } returns Completable.complete()
        every { remoteDataSource.getAllCategories() } returns Single.just(categories)

        repository.getAllCategories().test()
            .assertValue(categories)

        verify(exactly = 1) { localDataSource.setAllCategories(categories) }
    }

    @Test
    fun `validateCache completes without error when local data is valid`() {
        every { localDataSource.isCacheValid() } returns Single.just(true)

        repository.validateCache().test().assertComplete()

        verify(exactly = 1) { localDataSource.isCacheValid() }
        verify(exactly = 0) { remoteDataSource.getAllCategories() }
        verify(exactly = 0) { localDataSource.setAllCategories(any()) }
    }

    @Test
    fun `validateCache completes without error when local data is invalid and got updated from remote`() {
        every { localDataSource.isCacheValid() } returns Single.just(false)
        every { remoteDataSource.getAllCategories() } returns Single.just(categories)
        every { localDataSource.setAllCategories(categories) } returns Completable.complete()

        repository.validateCache().test().assertComplete()

        verify(exactly = 1) { localDataSource.isCacheValid() }
        verify(exactly = 1) { remoteDataSource.getAllCategories() }
        verify(exactly = 1) { localDataSource.setAllCategories(categories) }
    }

    @Test
    fun `exceptions from local get passed to observer`() {
        every { localDataSource.getAllCategories() } returns Maybe.error(RuntimeException())

        repository.getAllCategories().test()
            .assertError(RuntimeException::class.java)
    }

    @Test
    fun `exceptions from remote get passed to observer`() {
        every { localDataSource.getAllCategories() } returns Maybe.empty()
        every { remoteDataSource.getAllCategories() } returns Single.error(RuntimeException())

        repository.getAllCategories().test()
            .assertError(RuntimeException::class.java)
    }

}