package com.karzek.exercises.database.category

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.karzek.exercises.database.ExerciseDatabase
import com.karzek.exercises.database.equipment.EquipmentDao
import com.karzek.exercises.database.equipment.EquipmentEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

internal class EquipmentDaoTest {
    private lateinit var dao: EquipmentDao
    private lateinit var db: ExerciseDatabase

    private val entities = listOf(
        EquipmentEntity(1, "Equipment 1"),
        EquipmentEntity(2, "Equipment 2")
    )

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            context, ExerciseDatabase::class.java
        ).build()
        dao = db.equipmentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun successfulWriteAndRead() {
        dao.insertAll(entities)
        dao.getAll().test()
            .assertValue(entities)
    }

    @Test
    @Throws(Exception::class)
    fun successfulCount() {
        dao.insertAll(entities)
        dao.countItems().test()
            .assertValue(2)
    }

    @Test
    @Throws(Exception::class)
    fun successfulDeletion() {
        dao.insertAll(entities)
        dao.deleteAll()
        dao.getAll().test().assertValue(emptyList())
    }

    @Test
    @Throws(Exception::class)
    fun successfulOverwrite() {
        dao.insertAll(entities)
        dao.insertAll(entities)
        dao.getAll().test().assertValue(entities)
    }
}