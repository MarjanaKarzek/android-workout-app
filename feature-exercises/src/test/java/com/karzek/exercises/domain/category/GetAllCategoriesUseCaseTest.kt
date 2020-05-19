package com.karzek.exercises.domain.category

import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase.Input
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase.Output.ErrorUnknown
import com.karzek.exercises.domain.category.IGetAllCategoriesUseCase.Output.Success
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.domain.category.repository.ICategoryRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetAllCategoriesUseCaseTest : BaseUnitTest() {

    private val repository: ICategoryRepository = mockk()

    private val input = Input
    private val categories = listOf(
        Category(0, "Category 1"),
        Category(1, "Category 2")
    )

    private lateinit var useCase: IGetAllCategoriesUseCase

    @BeforeEach
    override fun setup() {
        super.setup()
        useCase = GetAllCategoriesUseCase(repository)
    }

    @Test
    fun `successful execution returns Success output`() {
        every { repository.getAllCategories() } returns Single.just(categories)

        useCase.execute(input).test()
            .assertValue(Success(categories))
    }

    @Test
    fun `exceptions get mapped to ErrorUnknown output`() {
        every { repository.getAllCategories() } returns Single.error(RuntimeException())

        useCase.execute(input).test()
            .assertValue(ErrorUnknown)
    }

}