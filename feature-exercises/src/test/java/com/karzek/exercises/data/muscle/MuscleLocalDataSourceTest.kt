package com.karzek.exercises.data.muscle

import android.content.SharedPreferences
import com.karzek.exercises.base.BaseUnitTest
import com.karzek.exercises.data.muscle.contract.IMuscleLocalDataSource
import com.karzek.exercises.database.muscle.MuscleDao
import com.karzek.exercises.database.muscle.MuscleEntity
import com.karzek.exercises.domain.muscle.model.Muscle
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

internal class MuscleLocalDataSourceTest : BaseUnitTest() {

    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)
    private val muscleDao: MuscleDao = mockk(relaxed = true)

    private lateinit var dataSource: IMuscleLocalDataSource

    private val muscleEntities = listOf(
        MuscleEntity(0, "Muscle 1"),
        MuscleEntity(1, "Muscle 2")
    )

    private val muscles = listOf(
        Muscle(0, "Muscle 1"),
        Muscle(1, "Muscle 2")
    )

    @BeforeEach
    override fun setup() {
        super.setup()
        dataSource = MuscleLocalDataSource(sharedPreferences, muscleDao)
    }

    @Test
    fun `getAllMuscles returns expected output when cache is valid`() {
        val currentTime = Date().time
        every { sharedPreferences.getLong("MUSCLE_CACHE_TIME_STAMP", any()) } returns currentTime
        every { muscleDao.getAll() } returns Single.just(muscleEntities)

        dataSource.getAllMuscles().test()
            .assertValue(muscles)
    }

    @Test
    fun `getAllMuscles returns Maybe empty output when cache is invalid`() {
        val currentTime = Date().time + 24 * 60 * 60 * 1000
        every { sharedPreferences.getLong("MUSCLE_CACHE_TIME_STAMP", any()) } returns currentTime

        dataSource.getAllMuscles().test()
            .assertEmpty()
    }

    @Test
    fun `setAllMuscles removes the old data sets it to the new data and updates the timestamp`() {
        dataSource.setAllMuscles(muscles).test()
            .assertComplete()

        verify(exactly = 1) { muscleDao.deleteAll() }
        verify(exactly = 1) { muscleDao.insertAll(muscleEntities) }
        verify(exactly = 1) { sharedPreferences.edit().putLong("MUSCLE_CACHE_TIME_STAMP", any()) }
    }

    @Test
    fun `exceptions from dao get passed to observer`() {
        val currentTime = Date().time
        every { sharedPreferences.getLong("MUSCLE_CACHE_TIME_STAMP", any()) } returns currentTime
        every { muscleDao.getAll() } returns Single.error(RuntimeException())

        dataSource.getAllMuscles().test()
            .assertError(RuntimeException::class.java)
    }
}