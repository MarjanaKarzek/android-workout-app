package com.karzek.exercises.data

import androidx.paging.PagedList
import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.exercise.PagedExerciseProvider
import com.karzek.exercises.data.exercise.contract.IExerciseLocalDataSource
import com.karzek.exercises.data.exercise.contract.IExerciseRemoteDataSource
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.domain.exercise.repository.IPagedExerciseProvider
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PagedExerciseProviderTest : BaseUnitTest() {

    private val remoteDataSource: IExerciseRemoteDataSource = mockk()
    private val localDataSource: IExerciseLocalDataSource = mockk()

    private val pagedDataSource: Observable<PagedList<Exercise>> = mockk()

    private lateinit var providerPaged: IPagedExerciseProvider

    @BeforeEach
    override fun setup() {
        super.setup()
        providerPaged = PagedExerciseProvider(remoteDataSource, localDataSource)
    }

//    @Test
//    fun `getExercises returns expected output`() {
//        every { localDataSource.getExercises() } returns pagedDataSource
//
//        assert(providerPaged.getExercises().test() == pagedDataSource)
//    }

}